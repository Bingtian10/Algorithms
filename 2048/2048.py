from __future__ import absolute_import, division, print_function
import pygame, sys, time, math, random
from pygame.locals import *
from ai import *

BLACK = (0, 0, 0)
RED = (244, 67, 54)
PINK = (234, 30, 99)
PURPLE = (156, 39, 176)
DEEP_PURPLE = (103, 58, 183)
BLUE = (33, 150, 243)
TEAL = (0, 150, 136)
L_GREEN = (139, 195, 74)
GREEN = (60, 175, 80)
ORANGE = (255, 152, 0)
DEEP_ORANGE = (255, 87, 34)
BROWN = (121, 85, 72)
COLORS = { 0:BLACK, 2:RED, 4:PINK, 8:PURPLE, 16:DEEP_PURPLE,
               32:BLUE, 64:TEAL, 128:L_GREEN, 256:GREEN,
               512:ORANGE, 1024: DEEP_ORANGE, 2048:BROWN, 
               4096:DEEP_PURPLE, 8192:DEEP_ORANGE, 16384:BROWN, 32768:TEAL}

class Game:
	def __init__(self):
		self.total_points = 0
		self.default_tile = 2
		self.board_size = 4
		pygame.init()
		self.surface = pygame.display.set_mode((400, 500), 0, 32)
		pygame.display.set_caption("2048")
		self.myfont = pygame.font.SysFont("arial", 40)
		self.scorefont = pygame.font.SysFont("arial", 30)
		self.tileMatrix = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]]
		self.undoMat = []
	def loop(self, fromLoaded = False):
		auto = False
		if not fromLoaded:
			self.placeRandomTile()
			self.placeRandomTile()
		self.printMatrix()
		while True:
			if auto:
				if self.checkIfCanGo():
					#Hint: Check the use of deepcopy
					ai = Gametree(copy.deepcopy(self.tileMatrix), 5, self.total_points)
					direction = ai.compute_decision() 
					self.move(direction)
				else:
					auto = False
					self.printGameOver()
			for event in pygame.event.get():
				if event.type == QUIT:
					pygame.quit()
					sys.exit()
				if event.type == KEYDOWN and event.key == K_RETURN:
					auto = not auto
				if self.checkIfCanGo():
					if event.type == KEYDOWN:
						if self.isArrow(event.key):
							direction = self.getRotations(event.key)
							self.move(direction)
				else:
					self.printGameOver()
				if event.type == KEYDOWN:
					if event.key == pygame.K_r:
						self.reset()
					if 50 < event.key and 56 > event.key:
						self.board_size = event.key - 48
						self.reset()
					if event.key == pygame.K_s:
						self.saveGameState()
					elif event.key == pygame.K_l:
						self.loadGameState()
					elif event.key == pygame.K_u:
						self.undo()
			pygame.display.update()
	def move(self, direction):
		self.addToUndo()
		for i in range(0, direction):
			self.rotateMatrixClockwise()
		if self.canMove():
			self.moveTiles()
			self.mergeTiles()
			self.placeRandomTile()
		for j in range(0, (4 - direction) % 4):
			self.rotateMatrixClockwise()
		self.printMatrix()
	def printMatrix(self):
		self.surface.fill(BLACK)
		for i in range(0, self.board_size):
			for j in range(0, self.board_size):
				pygame.draw.rect(self.surface, COLORS[self.tileMatrix[i][j]],
	                (i*(400/self.board_size), j*(400/self.board_size) + 100, 400/self.board_size, 400/self.board_size))
				label = self.myfont.render(str(self.tileMatrix[i][j]), 1, (255,255,255))
				label2 = self.scorefont.render("Score:" + str(self.total_points), 1, (255, 255, 255))
				self.surface.blit(label, (i*(400/self.board_size) + 30, j*(400/self.board_size) + 130))
				self.surface.blit(label2, (10, 20))
	def printGameOver(self):
		self.surface.fill(BLACK)
		label = self.scorefont.render("Game Over!", 1, (255,255,255))
		label2 = self.scorefont.render("Score:" + str(self.total_points), 1, (255,255,255))
		label3 = self.myfont.render("Press r to restart!", 1, (255,255,255))
		self.surface.blit(label, (50, 100))
		self.surface.blit(label2, (50, 200))
		self.surface.blit(label3, (50, 300))
	def placeRandomTile(self):
		while True:
			i = random.randint(0,self.board_size-1)
			j = random.randint(0,self.board_size-1)
			if self.tileMatrix[i][j] == 0:
				break
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
		return False
	def reset(self):
		self.total_points = 0
		self.surface.fill(BLACK)
		self.tileMatrix = [[0 for i in range(self.board_size)] for j in range(self.board_size)]
		self.loop()
	def canMove(self):
		tm = self.tileMatrix
		for i in range(0, self.board_size):
			for j in range(1, self.board_size):
				if tm[i][j-1] == 0 and tm[i][j] > 0:
					return True
				elif (tm[i][j-1] == tm[i][j]) and tm[i][j-1] != 0:
					return True
		return False
	def saveGameState(self):
		f = open("savedata", "w")
		line1 = " ".join([str(self.tileMatrix[int(x / self.board_size)][x % self.board_size])
	                    for x in range(0, self.board_size**2)])
		f.write(line1 + "\n")
		f.write(str(self.board_size)  + "\n")
		f.write(str(self.total_points))
		f.close()
	def loadGameState(self):
		f = open("savedata", "r")
		m = (f.readline()).split(' ', self.board_size ** 2)
		self.board_size = int(f.readline())
		self.total_points = int(f.readline())
		for i in range(0, self.board_size ** 2):
			self.tileMatrix[int(i / self.board_size)][i % self.board_size] = int(m[i])
		f.close()
		self.loop(True)
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
	def isArrow(self, k):
		return(k == pygame.K_UP or k == pygame.K_DOWN or k == pygame.K_LEFT or k == pygame.K_RIGHT)
	def getRotations(self, k):
		if k == pygame.K_UP:
			return 0
		elif k == pygame.K_DOWN:
			return 2
		elif k == pygame.K_LEFT:
			return 1
		elif k == pygame.K_RIGHT:
			return 3
	def convertToLinearMatrix(self):
		m = []
		for i in range(0, self.board_size ** 2):
			m.append(self.tileMatrix[int(i / self.board_size)][i % self.board_size])
		m.append(self.total_points)
		return m
	def addToUndo(self):
		self.undoMat.append(self.convertToLinearMatrix())
	def undo(self):
		if len(self.undoMat) > 0:
			m = self.undoMat.pop()
			for i in range(0, self.board_size ** 2):
				self.tileMatrix[int(i / self.board_size)][i % self.board_size] = m[i]
			self.total_points = m[self.board_size ** 2]
			self.printMatrix()

if __name__ == '__main__':
	game = Game()
	game.loop()
