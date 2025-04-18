#!/bin/bash

# Script to carefully remove commit activity comments
# This script is more precise, handling Java files differently

# Make sure we're in the git repository
cd "$(git rev-parse --show-toplevel)" || exit 1

# Clean simple files like README.md, pom.xml, and yml files
# These files have comments on separate lines
clean_simple_file() {
  local file=$1
  echo "Cleaning $file..."
  
  # Create a temp file for processing
  temp_file=$(mktemp)
  
  # Filter out lines that exactly match our commit activity marker pattern
  grep -v "^# Updated on .* - Commit activity$" "$file" | 
  grep -v "^<!-- Updated on .* - Commit activity -->$" > "$temp_file"
  
  # Only update if changes were made
  if ! cmp -s "$file" "$temp_file"; then
    cp "$temp_file" "$file"
    echo "  - Removed comments from $file"
  else
    echo "  - No simple comment patterns found in $file"
  fi
  
  # Clean up
  rm "$temp_file"
}

# Clean pom.xml specifically
clean_pom_xml() {
  local file=$1
  echo "Cleaning $file..."
  
  # Create a temp file for processing
  temp_file=$(mktemp)
  
  # The pom.xml may have comment lines appended to the end
  # We'll find where the </project> tag is and only keep lines up to that point
  last_line=$(grep -n "</project>" "$file" | head -1 | cut -d: -f1)
  
  if [ -n "$last_line" ]; then
    head -n "$last_line" "$file" > "$temp_file"
    
    # Only update if changes were made
    if ! cmp -s "$file" "$temp_file"; then
      cp "$temp_file" "$file"
      echo "  - Removed comments from $file"
    else
      echo "  - No changes needed for $file"
    fi
  else
    echo "  - Could not find </project> tag in $file, skipping"
  fi
  
  # Clean up
  rm "$temp_file"
}

# Main cleanup function that dispatches to specific cleaners
cleanup() {
  # Clean README.md - markdown file with simple comment patterns
  clean_simple_file "README.md"
  
  # Clean application.yml - YAML file with simple comment patterns
  clean_simple_file "src/main/resources/application.yml"
  
  # Clean pom.xml - XML file with special handling
  clean_pom_xml "pom.xml"
  
  # For HTML and Java files, we'll use git to restore them since they
  # have comments embedded with code, which makes them hard to clean automatically
  echo "Restoring Java and HTML files to their initial commit state..."
  git restore "src/main/java/com/dm/ecommerce/config/OpenApiConfig.java"
  git restore "src/main/java/com/dm/ecommerce/config/SecurityConfig.java"
  git restore "src/main/resources/static/index.html"
  
  echo "Cleanup complete."
}

# Execute the main cleanup function
cleanup 