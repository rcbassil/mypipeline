#!/usr/bin/env sh

function try()
{
    [[ $- = *e* ]]; SAVED_OPT_E=$?
    set +e
}

function throw()
{
    exit $1
}

function catch()
{
    export exception_code=$?
    (( $SAVED_OPT_E )) && set +e
    return $exception_code
}

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