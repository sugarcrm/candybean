#!/bin/sh

# check if homebrew is installed

command -v brew >/dev/null 2>&1 ||
{
echo >&2 "Homebrew not installed. Aborting.";
exit 1;
}

# check if appium is installed

command -v appium >/dev/null 2>&1 || 
{
echo >&2 "I require foo but it's not installed.  Aborting."; 
exit 1; 
}

