#!/bin/bash

# Remove the old package structure
rm -rf /Users/daniyal/spring-ecommerce-backend/src/main/java/com/example

# Make backup of original structure
mkdir -p /Users/daniyal/spring-ecommerce-backend/backup
cp -R /Users/daniyal/spring-ecommerce-backend/src/main/java/com/dm /Users/daniyal/spring-ecommerce-backend/backup/

echo "Package migration finalized. Old package structure removed."
echo "Backup of new structure created at /Users/daniyal/spring-ecommerce-backend/backup"
echo ""
echo "NEXT STEPS:"
echo "1. Verify your application.yml has any package references updated if needed"
echo "2. Run Maven build and test"
echo "3. Update any IDE configurations to reflect the new package structure" 