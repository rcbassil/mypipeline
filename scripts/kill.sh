#!/usr/bin/env sh

source trycatch.sh

try
(
echo 'The following command terminates the "npm start" process using its PID'
echo '(written to ".pidfile"), all of which were conducted when "deliver.sh"'
echo 'was executed.'
set -x
kill $(cat .pidfile)
)
catch || {
    case $exception_code in
        *)
            echo "Unknown error: $exit_code"
            throw $exit_code    # re-throw an unhandled exception
        ;;
    esac
}