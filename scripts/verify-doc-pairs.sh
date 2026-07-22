#!/bin/sh
set -eu

find docs -type f -name '*.md' -print | while IFS= read -r markdown; do
    html="${markdown%.md}.html"
    if [ ! -f "$html" ]; then
        echo "Missing human-view pair: $html" >&2
        exit 1
    fi
done

find docs -type f -name '*.html' -print | while IFS= read -r html; do
    markdown="${html%.html}.md"
    if [ ! -f "$markdown" ]; then
        echo "Missing canonical pair: $markdown" >&2
        exit 1
    fi
done
