#!/bin/bash

# Script to remove all the "Updated on" commit activity comments from files
# This undoes the changes made by generate_commits.sh

# Make sure we're in the git repository
cd "$(git rev-parse --show-toplevel)" || exit 1

# Function to show the before and after of a few lines around matching patterns for verification
show_matches() {
  local file=$1
  local pattern=$2
  
  echo "Checking for matches in $file..."
  grep -n "$pattern" "$file" || echo "  No matches found"
}

# Function to clean a file by removing any "Updated on" commit activity comments
clean_file() {
  local file=$1
  
  # Only process files that exist
  if [ ! -f "$file" ]; then
    echo "File not found: $file"
    return
  fi
  
  echo "Cleaning $file..."
  
  # Show matches before cleaning (for verification)
  case "${file##*.}" in
    java)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
    html)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
    yml|yaml|properties)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
    md)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
    xml)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
    *)
      show_matches "$file" "Updated on .* - Commit activity"
      ;;
  esac
  
  # Create a temp file for processing
  temp_file=$(mktemp)
  
  # Filter out lines containing commit activity markers
  grep -v "Updated on .* - Commit activity" "$file" > "$temp_file"
  
  # Only update if changes were made
  if ! cmp -s "$file" "$temp_file"; then
    cp "$temp_file" "$file"
    echo "  - Removed comments from $file"
  else
    echo "  - No changes made to $file"
  fi
  
  # Clean up
  rm "$temp_file"
}

# List of files to clean (same as in generate_commits.sh)
FILES=(
  "src/main/resources/application.yml"
  "src/main/resources/static/index.html"
  "README.md"
  "src/main/java/com/dm/ecommerce/config/OpenApiConfig.java"
  "src/main/java/com/dm/ecommerce/config/SecurityConfig.java"
  "pom.xml"
)

# Clean each file
for file in "${FILES[@]}"; do
  clean_file "$file"
done

echo "Cleanup complete. Check the output to verify changes." 