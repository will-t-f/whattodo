# Sorry for doing this in Java, right off the bat

FILE := Main
SRCE := $(FILE).java
COMP := javac

APPNAME := script
COMMAND := /usr/bin/whattodo

all:
	$(COMP) $(SRCE)

clean:
	rm -f *.class

install:
	mkdir -p /usr/bin
	cp $(APPNAME) $(COMMAND)
	mkdir -p $(DESTDIR)
	sed -i 's|#####|'$(DESTDIR)'|' $(COMMAND)
	chmod +x $(COMMAND)
	find . -name "*.class" -exec cp {} $(DESTDIR) \;
	mkdir -p ~/.config/whattodo/
	touch ~/.config/whattodo/main.todo

.PHONY: all clean
