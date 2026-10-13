# Testing

This page describes how SysKit is tested and how to run the tests yourself.

## Running the tests

```bash
shellcheck syskit.sh
python3 -m py_compile syskit.py
```

## What is covered

ShellCheck for scripts, `py_compile` for Python, and manual testing of menu actions. Never assume a destructive or package-management command is safe on every distribution without testing it.

## Adding tests

- Add tests alongside the code they cover.
- Prefer testing behavior over implementation details.
- Keep fixtures free of real personal data, keys, or credentials.

## Current status

Automated test coverage is a work in progress. Contributions that add tests are
welcome.
