# Sorry for doing this in Java, right off the bat

FILE := Main
SRCE := $(FILE).java
COMP := javac

APPNAME := whattodo
COMMAND := ~/bin/$(APPNAME)

all:
	$(COMP) $(SRCE)

clean:
	rm -f *.class

install:
	cp $(APPNAME) $(COMMAND)
	mkdir -p $(DESTDIR)
	sed -i 's|#####|'$(DESTDIR)'|' $(COMMAND)
	chmod +x $(APPNAME)
	find . -name "*.class" -exec cp {} $(DESTDIR) \;
	mkdir -p ~/.config/$(APPNAME)/
	touch ~/.config/$(APPNAME)/main.todo

.PHONY: all clean
