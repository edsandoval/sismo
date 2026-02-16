# worktree-delete.ps1
param(
    [Parameter(Mandatory=$true)]
    [string]$BranchName
)

$worktreeBasePath = "D:\Proyectos\GitHub\RamasParaAutopilot"
$mainRepoPath = "D:\Proyectos\GitHub\sismo"
$worktreePath = Join-Path $worktreeBasePath $BranchName

Set-Location $mainRepoPath

# Eliminar worktree
Write-Host "Eliminando worktree: $worktreePath"
git worktree remove $worktreePath --force 2>$null

# Eliminar branch
Write-Host "Eliminando branch: copilot/$BranchName"
git branch -D copilot/$BranchName 2>$null

# Eliminar test branch
Write-Host "Eliminando test branch: test/copilot/$BranchName"
git branch -D test/copilot/$BranchName 2>$null


Write-Host "Listo!"
