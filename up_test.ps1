# Cross-platform PowerShell script to run tests with Docker Compose
# Works on: Windows, macOS, Linux
# Requirement: PowerShell 7+ (pwsh)

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "  Running Tests with Docker Compose" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# Variables
$COMPOSE_FILE = "docker-compose.test.yml"
$PROJECT_NAME = "up_test"
$CONTAINER_NAME = "api"
$LOCAL_REPORT_DIR = "build\reports\tests\test"

# Detect OS
$IsWindows = $PSVersionTable.Platform -eq "Win32NT" -or $PSVersionTable.PSVersion.Major -lt 6

# Step 1: Run docker-compose
Write-Host "[1] Starting tests..." -ForegroundColor Green
docker-compose -p $PROJECT_NAME -f $COMPOSE_FILE up --build --abort-on-container-exit

Write-Host ""
Write-Host "[2] Getting container ID..." -ForegroundColor Cyan

# Step 2: Get container ID (the container should still exist even if stopped)
$container_id = docker-compose -p $PROJECT_NAME -f $COMPOSE_FILE ps -a -q $CONTAINER_NAME 2>$null

if ($container_id) {
    Write-Host "OK Container found: $container_id" -ForegroundColor Green
    Write-Host "[3] Preparing local folder..." -ForegroundColor Cyan

    # Create local directory if it does not exist
    if (Test-Path $LOCAL_REPORT_DIR) {
        Remove-Item -Path $LOCAL_REPORT_DIR -Recurse -Force -ErrorAction SilentlyContinue
    }
    New-Item -ItemType Directory -Force -Path $LOCAL_REPORT_DIR | Out-Null
    Write-Host "OK Local folder prepared: $LOCAL_REPORT_DIR" -ForegroundColor Green

    Write-Host "[4] Copying reports from container..." -ForegroundColor Cyan

    # Step 3: Copy reports from the container
    # First verify what exists inside the container
    Write-Host "   Checking container contents..." -ForegroundColor DarkGray
    $docker_result = & docker exec $container_id find /app/build/reports/tests -type f -name "*.html" 2>$null

    if ($docker_result) {
        Write-Host "   Files found in container:" -ForegroundColor DarkGray
        Write-Host $docker_result -ForegroundColor DarkGray
    }

    # Try to copy the full reports
    try {
        Write-Host "   Running: docker cp $container_id`:/app/build/reports/tests\. $(Get-Location)\$LOCAL_REPORT_DIR" -ForegroundColor DarkGray

        & docker cp "$container_id`:/app/build/reports/tests/." "$LOCAL_REPORT_DIR"

        Write-Host "OK Reports copied successfully" -ForegroundColor Green

        # Verify files were copied
        $files = Get-ChildItem -Path $LOCAL_REPORT_DIR -Recurse -ErrorAction SilentlyContinue
        if ($files) {
            Write-Host "OK Copied $($files.Count) files" -ForegroundColor Green
            Write-Host "   Content:" -ForegroundColor DarkGray
            $files | ForEach-Object { Write-Host "   - $($_.FullName.Replace((Get-Location).Path, ''))" -ForegroundColor DarkGray }
        }
        else {
            Write-Host "WARNING No copied files were found" -ForegroundColor Yellow
        }
    }
    catch {
        Write-Host "ERROR Exception while copying reports: $_" -ForegroundColor Red
    }
}
else {
    Write-Host "WARNING Container not found" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[5] Stopping containers..." -ForegroundColor Cyan

# Step 4: Bring containers down
docker-compose -p $PROJECT_NAME -f $COMPOSE_FILE down

# Wait a moment
Start-Sleep -Seconds 2

Write-Host ""
Write-Host "[6] Verifying report on local machine..." -ForegroundColor Cyan

# Step 5: Open the report
$REPORT_PATH = "build\reports\tests\test\index.html"
$REPORT_DIR = "build\reports\tests\test"

if (Test-Path $REPORT_PATH) {
    Write-Host "OK Report found: $REPORT_PATH" -ForegroundColor Green
    Write-Host ""
    Write-Host "====================================" -ForegroundColor Green
    Write-Host "     Tests executed successfully" -ForegroundColor Green
    Write-Host "  Opening report in the browser..." -ForegroundColor Green
    Write-Host "====================================" -ForegroundColor Green
    Write-Host ""

    if ($IsWindows) {
        Write-Host "Opening: $((Get-Item $REPORT_PATH).FullName)" -ForegroundColor Cyan
        Start-Process $REPORT_PATH
    }
    else {
        Write-Host "Opening: $REPORT_PATH" -ForegroundColor Cyan
    }
}
elseif (Test-Path $REPORT_DIR) {
    Write-Host "OK Reports folder found" -ForegroundColor Green
    Write-Host ""
    Write-Host "====================================" -ForegroundColor Cyan
    Write-Host "   Folder content:" -ForegroundColor Cyan
    Write-Host "====================================" -ForegroundColor Cyan
    Write-Host ""

    Get-ChildItem -Path $REPORT_DIR -Recurse | ForEach-Object {
        Write-Host "   - $($_.FullName.Replace((Get-Location).Path, ''))" -ForegroundColor DarkGray
    }

    Write-Host ""
    Write-Host "   Opening folder..." -ForegroundColor Cyan

    if ($IsWindows) {
        Invoke-Item $REPORT_DIR
    }
}
else {
    Write-Host "WARNING Report not found" -ForegroundColor Yellow
    Write-Host "   Expected path: $(Get-Location)\$REPORT_PATH" -ForegroundColor Yellow

    if (Test-Path "build\reports") {
        Write-Host "   Content of build\reports:" -ForegroundColor Yellow
        Get-ChildItem -Path "build\reports" -Recurse -ErrorAction SilentlyContinue | ForEach-Object {
            Write-Host "   - $($_.FullName.Replace((Get-Location).Path, ''))" -ForegroundColor DarkGray
        }
    }
    else {
        Write-Host "   ERROR: The build\reports folder does not exist" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "OK Process completed" -ForegroundColor Green
