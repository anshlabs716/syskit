# Development

This page describes how to set up a development environment and work on
SysKit.

## Requirements

- A Linux system (or Termux on Android)
- `gcc`, `make` for the C builds
- `bash`, `python3`, a JDK for the respective implementations

## Getting the code

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

## Build

```bash
make            # C implementations
# or:
python3 syskit.py
./syskit.sh
```

## Run

```bash
./syskit.sh
# or: python3 syskit.py
```

## Test

```bash
shellcheck syskit.sh
python3 -m py_compile syskit.py
```

## Workflow

- Branch from `main` (see [CONTRIBUTING.md](../CONTRIBUTING.md)).
- Keep changes focused and documented.
- Update `CHANGELOG.md` for user-facing changes.
