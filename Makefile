# Convenience build targets. Adjust to match the project's real entry points.

CC ?= cc
CFLAGS ?= -O2 -Wall -Wextra

.PHONY: all clean

all:
	@echo "No default build target configured yet. See docs/building.md."

clean:
	@echo "Nothing to clean."
