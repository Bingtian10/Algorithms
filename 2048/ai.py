from __future__ import absolute_import, division, print_function
import copy
import random
MOVES = {0: 'up', 1: 'left', 2: 'down', 3: 'right'}


class Gametree:
	"""main class for the AI"""
	# Hint: Two operations are important. Grow a game tree, and then compute minimax score.
	# Hint: To grow a tree, you need to simulate the game one step.
	# Hint: Think about the difference between your move and the computer's move.

	# Constructor that copy the information in the current game state, also constrcut a weightedMatrix
	def __init__(self, root_state, depth_of_tree, current_score): 
		self.root_state = root_state
		self.depth_of_tree = depth_of_tree
		self.current_score = current_score
		self.rootNode = Node(0, "maxPlayer", self.current_score, copy.deepcopy(self.root_state))
		self.weightMatrix = [[4**15, 4**14, 4**13, 4*12], [4**8, 4**9, 4**10, 4*11], 
		[4**7, 4**6, 4**5, 4**4], [1, 4, 16, 64]]
	
	# expectimax for computing best move
	def expectimax(self, node):
		if(node.terminal):
			weightScore = 0
			for i in range(len(self.weightMatrix)):
				for j in range(len(self.weightMatrix[i])):
					# Adding weight to preferable matrix where larger value at larger weighted tile should be prefered
					weightScore = weightScore + self.weightMatrix[i][j]*node.tileMatrix[i][j]
			return weightScore

		# Straightforward from slide
		elif(node.player == "maxPlayer"):
			# Since you can't have negative score, -1 should suffice to represent -infinity
			value = -1
			for child in node.children:
				value = max(value, self.expectimax(child))
			return value

		# Copy right from the slide
		elif(node.player == "chance"):
			value = 0
			for child in node.children:
				# Empty space is calculated in growTree(), which count the number of 0 in matrix
				value = value + self.expectimax(child) * (1/node.emptySpace)
			return value

		# Not possible to reach this step
		else:
			return -1


	# function to return best decision to game
	def compute_decision(self):
		# Should have at least one depth, else would've stop by the main program in 2048.py
		self.growTree(self.rootNode)
		index = 0
		valueList = []

		# Calculate expectiMax for each of 4 possible player move
		for child in self.rootNode.children:
			expectValue = self.expectimax(child)
			valueList.append(expectValue)

		# Find the maximum expectiMax value and figure out what action was taken to create this child
		max = valueList[0]
		for i in range(len(valueList)):
			if(valueList[i] > max):
				max = valueList[i]
				index = i
		return self.rootNode.action[index]

	# Function that grow the game state tree
	def growTree(self, node):
		if(node.player == "maxPlayer" and node.depth < 3):
			# Create a list representation of current state
			current_sim = Simulator(node.score, node.player, copy.deepcopy(node.tileMatrix))
			current_linear = current_sim.convertToLinearMatrix()
			for i in range(0, 4):
				sim = Simulator(node.score, node.player, copy.deepcopy(node.tileMatrix))
				# Check if current state would be possible to move
				notOver = sim.checkIfCanGo()
				if(notOver):
					sim.move(i)
					sim_linear = sim.convertToLinearMatrix()

					# If after move the matrix and score still the same, prune the tree
					# This means this branch could be computed by some other branch
					if(sim_linear == current_linear):
						continue

					#Increment depth, change player, update board state and tileMatrix
					else:
						newNode = Node(node.depth+1, "chance", sim.total_points,copy.deepcopy(sim.tileMatrix))
						# If we create leaf node at current state, mark terminal
						if(newNode.depth == 3):
							newNode.terminal = True
						# Action correspond to current i of whichever action is taken for each child
						node.action.append(i)
						node.children.append(newNode)
						# Recursively grow the tree
						self.growTree(newNode)

				# Board state is not able to move, no need to compute further more child
				else:
					break

		elif(node.player == "chance" and node.depth < 3):
			emptySpace = 0
			# Iterate current untouched chance player turn's matrix to count empty space, place tile at each empty space
			for i in range(len(node.tileMatrix)):
				for j in range(len(node.tileMatrix[i])):
					if (node.tileMatrix[i][j] == 0):
						emptySpace = emptySpace + 1
						sim = Simulator(node.score, node.player, copy.deepcopy(node.tileMatrix))
						sim.placeTile(i,j)
						newNode = Node(node.depth+1, "maxPlayer", sim.total_points, copy.deepcopy(sim.tileMatrix))
						node.children.append(newNode)
						self.growTree(newNode)
			node.emptySpace = emptySpace

		# Base case, no growing tree at depth 3
		else:
			return



# Helper class node for keep information about game state
class Node:
	def __init__(self, depth, player, score, tileMatrix):
		self.depth = depth
		self.player = player
		self.score = score
		self.tileMatrix = tileMatrix
		self.children = []
		self.terminal = False
		self.action = []

# Simulate game by one move, copy from 2048.py
class Simulator:
	def __init__(self, score, player, gameState):
		self.total_points = score
		self.player = player
		self.tileMatrix = gameState
		self.board_size = 4

	def move(self, direction):
		for i in range(0, direction):
			self.rotateMatrixClockwise()
		if self.canMove():
			self.moveTiles()
			self.mergeTiles()
		for j in range(0, (4 - direction) % 4):
			self.rotateMatrixClockwise()

	#Generating a tile for chance player
	def placeTile(self, i, j):
		self.tileMatrix[i][j] = 2

	def moveTiles(self):
		tm = self.tileMatrix
		for i in range(0, self.board_size):
			for j in range(0, self.board_size - 1):
				while tm[i][j] == 0 and sum(tm[i][j:]) > 0:
					for k in range(j, self.board_size - 1):
						tm[i][k] = tm[i][k + 1]
					tm[i][self.board_size - 1] = 0

	def mergeTiles(self):
		tm = self.tileMatrix
		for i in range(0, self.board_size):
			for k in range(0, self.board_size - 1):
				if tm[i][k] == tm[i][k + 1] and tm[i][k] != 0:
					tm[i][k] = tm[i][k] * 2
					tm[i][k + 1] = 0
					self.total_points += tm[i][k]
					self.moveTiles()

    # Check if current board state is movable
	def checkIfCanGo(self):
		tm = self.tileMatrix
		for i in range(0, self.board_size ** 2):
			if tm[int(i / self.board_size)][i % self.board_size] == 0:
				return True
		for i in range(0, self.board_size):
			for j in range(0, self.board_size - 1):
				if tm[i][j] == tm[i][j + 1]:
					return True
				elif tm[j][i] == tm[j + 1][i]:
					return True
    
    # Check if the board is able to move in the current board direction(After rotated)
	def canMove(self):
		tm = self.tileMatrix
		for i in range(0, self.board_size):
			for j in range(1, self.board_size):
				if tm[i][j-1] == 0 and tm[i][j] > 0:
					return True
				elif (tm[i][j-1] == tm[i][j]) and tm[i][j-1] != 0:
					return True
		return False

	# Convert matrix into a list with score as the last element
	def convertToLinearMatrix(self):
		m = []
		for i in range(0, self.board_size ** 2):
			m.append(self.tileMatrix[int(i / self.board_size)][i % self.board_size])
		m.append(self.total_points)
		return m

	def rotateMatrixClockwise(self):
		tm = self.tileMatrix
		for i in range(0, int(self.board_size/2)):
			for k in range(i, self.board_size- i - 1):
				temp1 = tm[i][k]
				temp2 = tm[self.board_size - 1 - k][i]
				temp3 = tm[self.board_size - 1 - i][self.board_size - 1 - k]
				temp4 = tm[k][self.board_size - 1 - i]
				tm[self.board_size - 1 - k][i] = temp1
				tm[self.board_size - 1 - i][self.board_size - 1 - k] = temp2
				tm[k][self.board_size - 1 - i] = temp3
				tm[i][k] = temp4

