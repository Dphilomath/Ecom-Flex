#!/bin/bash

# Script to generate realistic commit history over the last 100 days
# The commit frequency will vary to simulate real development patterns

# Make sure we're in the git repository
cd "$(git rev-parse --show-toplevel)" || exit 1

# Get today's date
TODAY=$(date +%s)

# Function to generate a random commit message
generate_commit_message() {
  local prefixes=("feat" "fix" "docs" "style" "refactor" "test" "chore" "perf")
  local components=("auth" "product" "cart" "order" "user" "category" "review" "security" "api" "ui" "config" "db")
  local actions=(
    "Add" "Update" "Fix" "Improve" "Refactor" "Optimize" "Remove" "Implement" 
    "Enhance" "Revise" "Simplify" "Correct" "Resolve" "Adjust" "Clean up"
  )
  local details=(
    "validation logic" "error handling" "method signatures" "documentation" "test coverage"
    "performance" "security vulnerabilities" "code style" "dependencies" "logging"
    "comments" "variable names" "file structure" "database queries" "API endpoints"
    "input sanitization" "edge cases" "UI components" "data models" "authentication flow"
  )
  
  local prefix=${prefixes[$RANDOM % ${#prefixes[@]}]}
  local component=${components[$RANDOM % ${#components[@]}]}
  local action=${actions[$RANDOM % ${#actions[@]}]}
  local detail=${details[$RANDOM % ${#details[@]}]}
  
  echo "$prefix($component): $action $detail"
}

# Function to make a small change to a file
make_change() {
  local file=$1
  local timestamp=$2
  
  # Only modify files that exist
  if [ ! -f "$file" ]; then
    return
  fi
  
  # Get the file extension
  local ext="${file##*.}"
  
  case $ext in
    java)
      # Add or modify a comment in Java files
      local line_num=$((RANDOM % $(wc -l < "$file") + 1))
      sed -i '' "${line_num}i\\
      // Updated on $(date -r "$timestamp" "+%Y-%m-%d") - Commit activity" "$file"
      ;;
    html|css|js)
      # Add or modify a comment in web files
      local line_num=$((RANDOM % $(wc -l < "$file") + 1))
      sed -i '' "${line_num}i\\
      <!-- Updated on $(date -r "$timestamp" "+%Y-%m-%d") - Commit activity -->" "$file"
      ;;
    properties|yml|yaml)
      # Add a comment in config files
      echo "# Updated on $(date -r "$timestamp" "+%Y-%m-%d") - Commit activity" >> "$file"
      ;;
    md)
      # For markdown files
      echo "<!-- Updated on $(date -r "$timestamp" "+%Y-%m-%d") - Commit activity -->" >> "$file"
      ;;
    *)
      # For other files, append a generic marker
      echo "# Updated on $(date -r "$timestamp" "+%Y-%m-%d") - Commit activity" >> "$file"
      ;;
  esac
}

# List of files to modify - focusing on files that exist in the project
FILES=(
  "src/main/resources/application.yml"
  "src/main/resources/static/index.html"
  "README.md"
  "src/main/java/com/dm/ecommerce/config/OpenApiConfig.java"
  "src/main/java/com/dm/ecommerce/config/SecurityConfig.java"
  "pom.xml"
)

# Generate commits over the last 100 days
for i in $(seq 100 -1 1); do
  # Calculate date i days ago
  DATE_TS=$((TODAY - i * 86400))
  DATE_STR=$(date -r $DATE_TS "+%Y-%m-%d")
  
  # Skip some days randomly to create a more realistic pattern
  if (( RANDOM % 3 == 0 )); then
    continue
  fi
  
  # Determine how many commits to make on this day (0-5)
  # Make more commits on weekdays, fewer on weekends
  DAY_OF_WEEK=$(date -r $DATE_TS "+%u")
  if (( DAY_OF_WEEK >= 6 )); then  # Weekend
    COMMITS_PER_DAY=$((RANDOM % 3))
  else  # Weekday
    COMMITS_PER_DAY=$((1 + RANDOM % 5))
  fi
  
  for j in $(seq 1 $COMMITS_PER_DAY); do
    # Set random time within the day
    HOUR=$((RANDOM % 14 + 8))  # Between 8 AM and 10 PM
    MINUTE=$((RANDOM % 60))
    SECOND=$((RANDOM % 60))
    
    # Calculate the complete timestamp
    COMMIT_TS=$((DATE_TS + HOUR * 3600 + MINUTE * 60 + SECOND))
    COMMIT_DATE=$(date -r $COMMIT_TS "+%Y-%m-%d %H:%M:%S")
    
    # Choose a random file to modify
    FILE=${FILES[$RANDOM % ${#FILES[@]}]}
    
    # Make a small change to the chosen file
    make_change "$FILE" "$COMMIT_TS"
    
    # Generate a commit message
    COMMIT_MSG=$(generate_commit_message)
    
    # Set the environment variables for the commit date
    export GIT_AUTHOR_DATE="$COMMIT_DATE"
    export GIT_COMMITTER_DATE="$COMMIT_DATE"
    
    # Add and commit the change
    git add "$FILE"
    git commit -m "$COMMIT_MSG" --date="$COMMIT_DATE"
    
    echo "Created commit on $COMMIT_DATE: $COMMIT_MSG"
    
    # Small delay to avoid overwhelming the system
    sleep 0.1
  done
done

echo "Commit history generation complete. Run 'git push' to upload to remote repository." 