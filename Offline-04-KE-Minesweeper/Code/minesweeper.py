import itertools
import random


class Minesweeper():
    """
    Minesweeper game representation
    """

    def __init__(self, height=8, width=8, mines=8):

        # Set initial width, height, and number of mines
        self.height = height
        self.width = width
        self.mines = set()

        # Initialize an empty field with no mines
        self.board = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                row.append(False)
            self.board.append(row)

        # Add mines randomly
        while len(self.mines) != mines:
            i = random.randrange(height)
            j = random.randrange(width)
            if not self.board[i][j]:
                self.mines.add((i, j))
                self.board[i][j] = True
        # self.mines.add((4,4))
        # self.board[4][4] = True
        # self.mines.add((7,4))
        # self.board[7][4] = True
        # self.mines.add((3,1))
        # self.board[3][1] = True
        # self.mines.add((5,4))
        # self.board[5][4] = True
        # self.mines.add((5,1))
        # self.board[5][1] = True
        # self.mines.add((2,3))
        # self.board[2][3] = True
        # self.mines.add((7,6))
        # self.board[7][6] = True
        # self.mines.add((0,5))
        # self.board[0][5] = True
        print("----------Mines-----------")
        for tempCell in self.mines:
            print("(",tempCell[0],",",tempCell[1],")")
        print("---------------------------")
        # At first, player has found no mines
        self.mines_found = set()

    def print(self):
        """
        Prints a text-based representation
        of where mines are located.
        """
        for i in range(self.height):
            print("--" * self.width + "-")
            for j in range(self.width):
                if self.board[i][j]:
                    print("|X", end="")
                else:
                    print("| ", end="")
            print("|")
        print("--" * self.width + "-")

    def is_mine(self, cell):
        i, j = cell
        return self.board[i][j]

    def nearby_mines(self, cell):
        """
        Returns the number of mines that are
        within one row and column of a given cell,
        not including the cell itself.
        """

        # Keep count of nearby mines
        count = 0

        # Loop over all cells within one row and column
        for i in range(cell[0] - 1, cell[0] + 2):
            # Ignore the cell itself
            if(i, cell[1]) == cell:
                continue
            if 0 <= i < self.height and 0 <= cell[1] < self.width:
                    if self.board[i][cell[1]]:
                        count += 1
        
        for j in range(cell[1] - 1, cell[1] + 2):
            if(cell[0], j) == cell:
                continue
            if 0 <= cell[0] < self.height and 0 <= j < self.width:
                    if self.board[cell[0]][j]:
                        count += 1

        return count

    def won(self):
        """
        Checks if all mines have been flagged.
        """
        return self.mines_found == self.mines


class Sentence():
    """
    Logical statement about a Minesweeper game
    A sentence consists of a set of board cells,
    and a count of the number of those cells which are mines.
    """

    def __init__(self, cells, count):
        self.cells = set(cells)
        self.count = count

    def __eq__(self, other):
        return self.cells == other.cells and self.count == other.count

    def __str__(self):
        return f"{self.cells} = {self.count}"

    def known_mines(self):
        """
        Returns the set of all cells in self.cells known to be mines.
        """
        if len(self.cells) == self.count:
            return self.cells
        else:
            return None
        
        #raise NotImplementedError

    def known_safes(self):
        """
        Returns the set of all cells in self.cells known to be safe.
        """
        if self.count == 0:
            return self.cells
        else:
            return None
        #raise NotImplementedError

    def mark_mine(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be a mine.
        """
        tempCells = list(self.cells)
        for tempCell in tempCells:
            if tempCell == cell:
                tempCells.remove(tempCell)
                self.count = self.count - 1
        self.cells = set(tempCells)
        
        #raise NotImplementedError

    def mark_safe(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be safe.
        """
        tempCells = list(self.cells)
        for tempCell in tempCells:
            if tempCell == cell:
                tempCells.remove(tempCell)
        self.cells = set(tempCells)
        
        #raise NotImplementedError


class MinesweeperAI():
    """
    Minesweeper game player
    """

    def __init__(self, height=8, width=8):

        # Set initial height and width
        self.height = height
        self.width = width

        # Keep track of which cells have been clicked on
        self.moves_made = set()

        # Keep track of cells known to be safe or mines
        self.mines = set()
        self.safes = set()

        # List of sentences about the game known to be true
        self.knowledge = []

    def mark_mine(self, cell):
        """
        Marks a cell as a mine, and updates all knowledge
        to mark that cell as a mine as well.
        """
        self.mines.add(cell)
        for sentence in self.knowledge:
            sentence.mark_mine(cell)

    def mark_safe(self, cell):
        """
        Marks a cell as safe, and updates all knowledge
        to mark that cell as safe as well.
        """
        self.safes.add(cell)
        for sentence in self.knowledge:
            sentence.mark_safe(cell)

    
    def available(self, i, j):
        if (i,j) not in self.safes and (i,j) not in self.mines:
            return True
        return False
    
    
    def add_knowledge(self, cell, count):
        """
        Called when the Minesweeper board tells us, for a given
        safe cell, how many neighboring cells have mines in them.

        This function should:
            1) mark the cell as a move that has been made
            2) mark the cell as safe
            3) add a new sentence to the AI's knowledge base
               based on the value of `cell` and `count`
            4) mark any additional cells as safe or as mines
               if it can be concluded based on the AI's knowledge base
            5) add any new sentences to the AI's knowledge base
               if they can be inferred from existing knowledge
        """
        
        self.mark_safe(cell)
        self.moves_made.add(cell)
        
        #getting neighbours
        row, col = cell
        neighbours = []
        if col-1>=0 and self.available(row, col-1):
            neighbours.append((row, col-1))
        if (row, col-1) in self.mines:
            count = count - 1
        if col+1<self.width and self.available(row, col+1):
            neighbours.append((row, col+1))
        if (row, col+1) in self.mines:
            count = count - 1
        if row-1>=0 and self.available(row-1, col):
            neighbours.append((row-1, col))
        if (row-1, col) in self.mines:
            count = count - 1
        if row+1<self.height and self.available(row+1, col):
            neighbours.append((row+1, col))
        if (row+1, col) in self.mines:
            count = count - 1

        # new sentence
        sentence = Sentence(neighbours, count)
        self.knowledge.append(sentence)

        # inference
        inferences = []
        for s in self.knowledge:
            if s == sentence:
                continue
            elif s.cells.issuperset(sentence.cells):
                setDiff = s.cells-sentence.cells
                if s.count == sentence.count: # safe
                    for tempCellSafe in setDiff:
                        self.mark_safe(tempCellSafe)
                elif len(setDiff) == s.count - sentence.count: # mine
                    for tempCellMine in setDiff:
                        print("Mine marked at:", tempCellMine[0], tempCellMine[1])
                        self.mark_mine(tempCellMine)
                else:
                    inferences.append(Sentence(setDiff, s.count - sentence.count))
            elif sentence.cells.issuperset(s.cells):
                setDiff = sentence.cells-s.cells
                if s.count == sentence.count: # safe
                    for tempCellSafe in setDiff:
                        self.mark_safe(tempCellSafe)
                elif len(setDiff) == sentence.count - s.count: # mine
                    for tempCellMine in setDiff:
                        print("Mine marked at:", tempCellMine[0], tempCellMine[1])
                        self.mark_mine(tempCellMine)
                else:
                    inferences.append(Sentence(setDiff, sentence.count - s.count))
        
        self.knowledge.extend(inferences)

        uniques = []
        for s in self.knowledge:
            if s not in uniques:
                uniques.append(s)
        self.knowledge = uniques

        finals = []
        for s in self.knowledge:
            finals.append(s)
            if s.known_mines():
                for tempCell in s.known_mines():
                    self.mark_mine(tempCell)
                finals.pop()
            elif s.known_safes():
                for tempCell in s.known_safes():
                    self.mark_safe(tempCell)
                finals.pop()
        self.knowledge = finals
        
        
        #raise NotImplementedError

    def make_safe_move(self):
        """
        Returns a safe cell to choose on the Minesweeper board.
        The move must be known to be safe, and not already a move
        that has been made.

        This function may use the knowledge in self.mines, self.safes
        and self.moves_made, but should not modify any of those values.
        """
        for cell in self.safes:
            if cell not in self.moves_made:
                return cell
        
        return None
        #raise NotImplementedError

    def make_random_move(self):
        """
        Returns a move to make on the Minesweeper board.
        Should choose randomly among cells that:
            1) have not already been chosen, and
            2) are not known to be mines
        """
        triedCells = set()

        for i in range(self.height):
            for j in range(self.width):
                if (i,j) not in self.mines and (i,j) not in self.moves_made:
                    triedCells.add((i,j))
        # No moves left
        if len(triedCells) == 0:
            return None
        # Return available
        move = random.choice(tuple(triedCells))
        return move
        
        # return None
        
        #raise NotImplementedError
