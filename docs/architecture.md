# Architecture

High-level structure of SysKit.

This page describes the high-level structure of SysKit. It is a starting
point and is updated as the implementation evolves.

## Overview

SysKit

## Main components

- **C (GTK3)**
- **Bash**
- **Python**
- **Java**
- **Kotlin / Compose**

## Data flow

Inputs (user actions, sensors, files or network requests) are processed by the
application logic and persisted or rendered as appropriate. Data handling is
described in [configuration.md](configuration.md) and, where relevant, in the
privacy documentation.

## Repository layout

See the project [README](../README.md) for the directory layout and build entry
points.

## Decisions

Significant design decisions are recorded under [adr/README.md](adr/README.md).
