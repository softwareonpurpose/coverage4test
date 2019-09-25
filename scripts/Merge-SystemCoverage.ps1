param (
    [parameter(ValueFromPipeline)]
    [ValidateNotNullOrEmpty()]
    [string] $Suite
    );
$systemReport = $null;
$reportPath = ".\reports\$Suite";
$systemFilename = "$reportPath\system.rpt";
$requirementsFilename = "$reportPath\requirements.rpt";
if(Test-Path $systemFilename){
    Remove-Item $systemFilename;
    }
if(Test-Path $requirementsFilename){
    Remove-Item $requirementsFilename;
    }
$systemReport = '@{system_coverage:[]';
Get-ChildItem $reportPath | Where-Object{($_.Name).Contains('system.rpt')} | ForEach-Object {$systemReport = $systemReport + (Get-Content $_.FullName | ConvertFrom-Json).};
$systemReport | ConvertTo-Json;