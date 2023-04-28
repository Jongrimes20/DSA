import pygame, sys
import random
print() #Print is so seperate some junk pygame text after importing
#Pygame is a library that will help render the maze graphically

#Object that has an initial constructor to have all walls up
class Cell:
    def __init__(self, xpos, ypos, id):
        self.northWall = 1
        self.eastWall = 1
        self.southWall = 1
        self.westWall = 1
        self.northCell = None
        self.eastCell = None
        self.southCell = None
        self.westCell = None
        self.x = xpos
        self.y = ypos
        self.id = id

#Disjoint set class for union and find operations
class Disjoint:
    #Initializing constructor, creates a set based on the array input'\
    disjointSet = list()
    def __init__(self, inputArray):
        self.disjointSet = []
        #Converting to set and back to remove duplicates and maintain set property
        inputToSet = set(inputArray)
        for i in list(inputToSet):
            #Appending all items from input to new disjoint set
            self.disjointSet.append([i])
    
    #Returns object set
    def getSet(self):
        return self.disjointSet

    #Function for combining two disjoint sets
    def union(self, fir, sec):
        #Getting the set index of the first and second elements for joining
        firIndex = self.findSetIndex(fir)
        secIndex = self.findSetIndex(sec)

        #Checking if there exist elements in both sets and if the two sets are not the same
        if not (firIndex == secIndex) and not (firIndex is None) and not (secIndex is None):
            #Creating a temp new set that combines the two sets
            tempSet = self.disjointSet[secIndex] + self.disjointSet[firIndex]

            #Deleting first set and replacing the second set with the combination
            self.disjointSet[secIndex] = tempSet
            del self.disjointSet[firIndex]

        #Returning the updated set
        return self.disjointSet

    def find(self, fir):
        for i in self.disjointSet:
            if fir in i:
                #Returning the element if found
                firIndex = self.disjointSet.index(i)
                return self.disjointSet[firIndex]
            
        #If not found, return none
        return None
    
    def findById(self, id):
        for i in self.disjointSet:
            for j in i:
                if j.id == id:
                    return j
        return None

    #Function that returns the index of an element
    def findSetIndex(self, fir):
        #Loops through the collection of disjoint sets
        for i in self.disjointSet:
            #If the element exists in one of the sets, return the index of that set
            if fir in i:
                elementIndex = self.disjointSet.index(i)
                return elementIndex
        #Return none if no index was found
        return None

#convert nodes to vertexes and implement Dijkstra
class Vertex:
    def __init__(self, cell):
        self.known = False
        self.adj = list()
        self.distance = 1000000
        self.path = None
        self.cell = cell

def printPath(vertex, pathList):
    # print(vertex.path)
    if(vertex.path is not None):
        printPath(vertex.path, pathList)
    pathList.append(vertex.cell.id)

#Creates an adjecency set for the vertexes based on walls knocked down
def createAdj(set):
    for vertex in set:
        if vertex.cell.northCell is not None:
            for match in set:
                if match.cell == vertex.cell.northCell:
                    vertex.adj.append(match)
        if vertex.cell.southCell is not None:
            for match in set:
                if match.cell == vertex.cell.southCell:
                    vertex.adj.append(match)
        if vertex.cell.eastCell is not None:
            for match in set:
                if match.cell == vertex.cell.eastCell:
                    vertex.adj.append(match)
        if vertex.cell.westCell is not None:
            for match in set:
                if match.cell == vertex.cell.westCell:
                    vertex.adj.append(match)

#Dijkstra algorithm
def dijkstra(vertex, set):
    vertex.distance = 0
    isUnknown = True

    while(isUnknown):
        #If there is an unknown element, break
        isUnknown = False
        for i in set:
            if i.known == False:
                isUnknown = True
                break
        if not isUnknown:
            break

        #Finding the smallest unkown distance vertex
        smallestVertex = None
        for i in set:
            if i.known == False:
                smallestVertex = i
                break

        #Check if there exists any closer distance
        for i in set:
            if i.distance < smallestVertex.distance and i.known == False:
                smallestVertex = i
            
        #Set the found vertex to known
        smallestVertex.known = True
        for i in smallestVertex.adj:
            if not i.known:
                # print("Big: " + str(smallestVertex.distance + 1))
                # print("Small: " + str(i.distance))
                if(smallestVertex.distance + 1 < i.distance):
                    # print("hit")
                    i.distance = smallestVertex.distance + 1
                    i.path = smallestVertex

#Converts the vertex path list to cardinal directions
def listToCoords(idList):
    fullString = ""
    prevId = idList[0]
    for i in idList[1:]:
        #East
        if i == prevId + 1:
            fullString += "E"
        #West
        if i == prevId - 1:
            fullString += "W"
        #South
        if i == prevId + mazeWidth:
            fullString += "S"
        #North
        if i == prevId - mazeWidth:
            fullString += "N"
        prevId = i
    return fullString

#Draws the graphical path pased on the coordnate string
def drawPath(screens, coordList):
    xoff = 25
    yoff = 25
    for char in coordList:
        if char == 'N':
            pygame.draw.rect(screens, (0,0,255), (xoff, yoff-50, 6, 50))
            yoff -= 50
        if char == 'E':
            pygame.draw.rect(screens, (0,0,255), (xoff, yoff, 50, 6))
            xoff += 50
        if char == 'S':
            pygame.draw.rect(screens, (0,0,255), (xoff, yoff, 6, 50))
            yoff += 50
        if char == 'W':
            pygame.draw.rect(screens, (0,0,255), (xoff-50, yoff, 50, 6))
            xoff -= 50

#Taking user input and declaring main maze array
print("Maze Generator")
mazeWidth = int(input("Enter Maze width(n): "))
mazeHeight = int(input("Enter Maze height(m): "))
maze = [[0 for i in range(mazeHeight)] for j in range(mazeWidth)] 

#Initializing the pygame scene for rending purposes
print()
pygame.init()
screen = pygame.display.set_mode((50*mazeHeight,50*mazeWidth))
screen.fill((0,0,0)) #fill black

#Creating the initial maze will ALL walls up
id = 0
for i in range(mazeWidth):
    for j in range(mazeHeight):
        maze[i][j] = Cell(j*50, i*50, id)
        id+=1
startingPos = maze[0][0]
endingPos = maze[mazeWidth-1][mazeHeight-1]

#Flattens the 2D array so we can use disjoint set and creates disjoint set obj
mainDisjointSet = Disjoint(sum(maze, []))

#while not mainDisjointSet.findSetIndex(startingPos) == mainDisjointSet.findSetIndex(endingPos):
while len(mainDisjointSet.getSet()) > 1:
    #1-N 2-S 3-E 4-W
    #Determining which random wall to break down
    wallToBreak = random.randint(1,4)

    #Determining which cell to break down by generating random id
    cellIdToBreak = random.randint(0, mazeWidth*mazeHeight-1)
    cellToBreak = mainDisjointSet.findById(cellIdToBreak)

    #Breaking down the walls and giving the two cells relation to eachother
    if wallToBreak == 1 and cellToBreak.y > 0:
        northCell = mainDisjointSet.findById(cellIdToBreak-mazeWidth)#
        if not mainDisjointSet.findSetIndex(northCell) == mainDisjointSet.findSetIndex(cellToBreak):
            cellToBreak.northWall = 0
            cellToBreak.northCell = northCell
            northCell.southCell = cellToBreak
            mainDisjointSet.union(northCell, cellToBreak)
    elif wallToBreak == 2 and cellToBreak.y < mazeWidth*50-50:
        southCell = mainDisjointSet.findById(cellIdToBreak+mazeWidth)
        if not mainDisjointSet.findSetIndex(southCell) == mainDisjointSet.findSetIndex(cellToBreak):
            cellToBreak.southWall = 0
            cellToBreak.southCell = southCell
            southCell.northCell = cellToBreak
            mainDisjointSet.union(southCell, cellToBreak)
    elif wallToBreak == 3 and cellToBreak.x < mazeWidth*50-50:
        eastCell = mainDisjointSet.findById(cellIdToBreak+1)
        if not mainDisjointSet.findSetIndex(eastCell) == mainDisjointSet.findSetIndex(cellToBreak):
            cellToBreak.eastWall = 0
            cellToBreak.eastCell = eastCell
            eastCell.westCell = cellToBreak
            mainDisjointSet.union(eastCell, cellToBreak)
    elif wallToBreak == 4 and cellToBreak.x > 0:
        westCell = mainDisjointSet.findById(cellIdToBreak-1)
        if not mainDisjointSet.findSetIndex(westCell) == mainDisjointSet.findSetIndex(cellToBreak):
            cellToBreak.westWall = 0
            cellToBreak.westCell = westCell
            westCell.eastCell = cellToBreak
            mainDisjointSet.union(westCell, cellToBreak)

#Converting set to graph
setToGraph = mainDisjointSet.getSet()
vertexs = list()
for i in setToGraph[0]:
    vertexs.append(Vertex(i))

#Creating adjacency list for the graph
createAdj(vertexs)

#Finding the main and last vertexes
mainVertex = None
lastVertex = None
for i in vertexs:
    if i.cell == mainDisjointSet.findById(0):
        mainVertex = i
    elif i.cell == mainDisjointSet.findById(mazeWidth*mazeHeight-1):
        lastVertex = i

#Performing dijkstra on main vertex
dijkstra(mainVertex, vertexs)

#Getting the path, converting to coords, and printing it out
listOfPath = list()
printPath(lastVertex, listOfPath)
coordList = listToCoords(listOfPath)
print("Path: " + listToCoords(listOfPath))

#Drawing the maze using pygame
for i in range(mazeWidth):
    for j in range(mazeHeight):
        cell = maze[i][j]
        pygame.draw.rect(screen, (0,0,0), (cell.x, cell.y, 50, 50), width=3)

#Marking Start and End positions
pygame.draw.rect(screen, (255,0,0), (startingPos.x+13, startingPos.y+13, 25, 25))
pygame.draw.rect(screen, (255,0,0), (endingPos.x+13, endingPos.y+13, 25, 25))

#Visually knocking down walls
for i in range(mazeWidth): 
    for j in range(mazeHeight):
        cell = maze[i][j]
        # Top Hide
        if cell.northWall == 0:
            pygame.draw.rect(screen, (255,255,255), (cell.x+3, cell.y-3, 44, 6))
        # Right Hide
        if cell.eastWall == 0:
            pygame.draw.rect(screen, (255,255,255), (cell.x+47, cell.y+3, 6, 44))
        # Bottom Hide
        if cell.southWall == 0:
            pygame.draw.rect(screen, (255,255,255), (cell.x+3, cell.y+47, 44, 6))
        # Left Hide
        if cell.westWall == 0:
            pygame.draw.rect(screen, (255,255,255), (cell.x-3, cell.y+3, 6, 44))

#Update the rending and display the screen
print("Click on the screen to display path!!")
pygame.display.update()
while True:
    for event in pygame.event.get():
        #Keep screen open until screen is quit
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()
        #If the user presses the mouse down, draw the path
        if event.type == pygame.MOUSEBUTTONDOWN:
            drawPath(screen, coordList)
    #Rendering
    pygame.display.update()
