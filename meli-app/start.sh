#!/bin/bash

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=========================================="
echo "🚀 MELI-APP – Start Script"
echo -e "==========================================${NC}"
echo ""
echo "Use: ./start.sh [profile]"
echo "or write the script when it's required."
echo ""
echo "Examples:"
echo "   ./start.sh dev"
echo "   ./start.sh test"
echo "   ./start.sh prod"
echo ""

if [ -z "$1" ]; then
    read -p "👉 Add the profile (dev / test / prod): " SPRING_PROFILES_ACTIVE
else
    SPRING_PROFILES_ACTIVE=$1
fi

echo ""
echo "Profile Selected: $SPRING_PROFILES_ACTIVE"
echo "------------------------------------------"

# Convert to lowercase for comparison
PROFILE_LOWER=$(echo "$SPRING_PROFILES_ACTIVE" | tr '[:upper:]' '[:lower:]')

case "$PROFILE_LOWER" in
    dev)
        echo -e "${BLUE}🧩 Running MELI-APP in DEVELOP mode...${NC}"
        mvn spring-boot:run
        ;;
    test)
        echo -e "${YELLOW}🧪 Running tests in TEST mode...${NC}"
        mvn test
        ;;
    prod)
        echo -e "${GREEN}🚀 Running MELI-APP in PRODUCTION mode...${NC}"
        if [ -f .env ]; then
            echo "Loading variables from .env..."
            export $(cat .env | grep -v '^#' | xargs)
        else
            echo -e "${RED}⚠️  The .env file is not found, make sure that it exists and you have it configured.${NC}"
        fi
        mvn spring-boot:run
        ;;
    *)
        echo -e "${RED}❌ Invalid profile. Use 'dev', 'test' or 'prod'.${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}=========================================="
echo "✅ Execution Finalized."
echo -e "==========================================${NC}"