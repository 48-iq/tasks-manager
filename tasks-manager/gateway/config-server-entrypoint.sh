#!/bin/bash

set -e

host="config-server"
port="8888"
cmd="$@"

>&2 echo "Waiting for $host:$port..."

until curl http://"$host":"$port"; do
    >&2 echo "config-server is unavailable - sleeping"
    sleep 1
done

>&2 echo "config-server is up - executing command"
exec $cmd