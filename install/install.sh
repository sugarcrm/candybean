#!/bin/bash

# check if homebrew is installed

command -v brew >/dev/null 2>&1 ||
{
echo >&2 "Homebrew not installed. Installing...";
ruby -e "$(curl -fsSL https://raw.github.com/mxcl/homebrew/go)"
brew doctor
}

# check if node/npm is installed

command -v node >/dev/null 2>&1 

if [ $? -eq 0 ]; then
    nodeversion=`node -e 'console.log(process.versions.node)'`

    oIFS="$IFS"
    IFS=.
    set -- $nodeversion
    IFS="$oIFS"

    if [ 8 -gt $2 ]
    then
        echo "Node version must be >= 0.8. Upgrading to latest version..."
        echo "Updating Homebrew..."
        brew update -v
        echo "Homebrew updated succesfully."
        echo "Upgrading node to latest version."
        brew upgrade node
    fi
else
    echo >&2 "Node.js not installed. Installing...";
    brew update 
    brew install node
fi

# check if android sdk is installed

command -v android >/dev/null 2>&1 ||
{
echo >&2 "Android SDK not installed. Installing...";
brew update
brew install android-sdk
}

# check if Xcode is installed

xcode-select -print-path >/dev/null 2>&1
if [ $? -ne 0 ]; then
    echo >&2 "Xcode not installed. Please install and rerun script."
    exit 1;
fi


# check if Xcode command line tools (instruments) is installed

command -v instruments >/dev/null 2>&1 ||
{
echo >&2 "Xcode command line tools not installed. Please install and rerun script."
exit 1;
}


# check if appium is installed

# command -v appium >/dev/null 2>&1 || 
# {
# echo >&2 "Appium not installed.  Installing...";
# npm install -g appium 
# }

echo "All dependencies installed."
