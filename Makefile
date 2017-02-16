TARGET   = eyepatch

CC       = g++
CFLAGS   = -std=c++11 -w

LINKER   = g++ -o
LFLAGS   = -Wall

SRCDIR   = src
OBJDIR   = obj
BINDIR   = bin

SOURCES  := $(wildcard $(SRCDIR)/*.cpp)
INCLUDES := $(wildcard $(SRCDIR)/*.h)
OBJECTS  := $(SOURCES:$(SRCDIR)/%.cpp=$(OBJDIR)/%.o)
rm       = rm -f


clean:
	rm -rf *.o
	rm -rf *.gch

build: $(SOURCES) $(INCLUDES)
	$(CC) $(CFLAGS) $(INCLUDES) $(SOURCES) -o eyepatch

game: player.o pirate.o src/game.h src/game.cpp
	$(CC) $(CFLAGS) -o src/game.h src/game.cpp player.o pirate.o
	
player.o: pirate.o src/player.h src/player.cpp src/booty.h
	$(CC) $(CFLAGS) -o obj/player.o src/player.cpp pirate.o
	
pirate.o: src/pirate.h src/pirate.cpp
	$(CC) $(CFLAGS) -o obj/pirate.o src/pirate.cpp

main.o:
	g++ $(CFLAGS) -o -c src/main.cpp
