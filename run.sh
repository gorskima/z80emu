#!/bin/sh

echo "[Altair 8800 emulator]"
echo 
echo "After the emulation starts, use Ctrl+K to quit"
echo
echo "Press any key to continue..."

read
clear

# Disable line editing, echo, NL<->CR conversions... just put us in old-school raw mode
# Remap interrupt key to Ctrl+K
stty cbreak -echo -icrnl -onlcr -istrip intr ^K

java -jar target/z80emu-1.0-SNAPSHOT.jar

# Restore "normal" terminal settings
stty sane
echo
