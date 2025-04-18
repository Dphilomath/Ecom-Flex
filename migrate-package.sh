#!/bin/bash

# Set directories
SRC_DIR="/Users/daniyal/spring-ecommerce-backend/src/main/java/com/example/ecommerce"
DEST_DIR="/Users/daniyal/spring-ecommerce-backend/src/main/java/com/dm/ecommerce"

# Create destination directories if they don't exist
find "$SRC_DIR" -type d | sed "s|$SRC_DIR|$DEST_DIR|" | xargs -I{} mkdir -p {}

# Process each Java file
find "$SRC_DIR" -type f -name "*.java" | while read -r file; do
    # Get the destination file path
    dest_file=$(echo "$file" | sed "s|$SRC_DIR|$DEST_DIR|")
    
    # Create new file with updated package name
    sed 's/package com.example.ecommerce/package com.dm.ecommerce/g' "$file" > "$dest_file"
    
    # Update all import statements
    sed -i '' 's/import com.example.ecommerce/import com.dm.ecommerce/g' "$dest_file"
    
    echo "Processed: $dest_file"
done

echo "Package migration completed. Please verify all references have been updated." 