$ErrorActionPreference = 'Stop'
$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$utf8 = New-Object System.Text.UTF8Encoding($false)
$lines = [Collections.Generic.List[string]]::new()
@('@startuml dominio', "' Gerado exclusivamente de src/modelo da versao principal.", 'top to bottom direction',
'skinparam backgroundColor white', 'skinparam shadowing false', 'skinparam roundcorner 0',
'skinparam classAttributeIconSize 0', 'skinparam defaultFontName Arial', 'skinparam defaultFontSize 18',
'skinparam classBackgroundColor white', 'skinparam classBorderColor #222222',
'skinparam classFontColor black', 'skinparam classFontStyle bold',
'skinparam ArrowColor #222222', 'skinparam linetype ortho', 'skinparam nodesep 100',
'skinparam ranksep 100', 'skinparam dpi 300', 'hide circle', '') | ForEach-Object { $lines.Add($_) }
$inventory = @()
foreach ($name in @('Pessoa','Aluno','Treinador','Turma','Matricula','Pagamento')) {
    $path = Join-Path $projectRoot ("src\modelo\" + $name + '.java')
    $text = [IO.File]::ReadAllText($path)
    $isAbstract = $text -match 'public abstract class'
    $lines.Add($(if ($isAbstract) { "abstract class $name {" } else { "class $name {" }))
    $attrs = @()
    $ops = @()
    foreach ($m in [regex]::Matches($text, '(?m)^\s*private\s+(\w+)\s+(\w+)\s*;')) {
        $attrs += '- ' + $m.Groups[2].Value + ': ' + $m.Groups[1].Value
    }
    foreach ($m in [regex]::Matches($text, '(?m)^\s*(public|protected)\s+(?:(\w+)\s+)?(\w+)\(([^)]*)\)\s*\{')) {
        $sign = if ($m.Groups[1].Value -eq 'public') { '+' } else { '#' }
        $params = @()
        foreach ($param in ($m.Groups[4].Value -split ',')) {
            if ($param.Trim()) {
                $parts = $param.Trim() -split '\s+'
                $params += $parts[1] + ': ' + $parts[0]
            }
        }
        $op = $sign + ' ' + $m.Groups[3].Value + '(' + ($params -join ', ') + ')'
        if ($m.Groups[2].Value) { $op += ': ' + $m.Groups[2].Value }
        $ops += $op
    }
    foreach ($attr in $attrs) { $lines.Add('  ' + $attr) }
    $lines.Add('  --')
    foreach ($op in $ops) { $lines.Add('  ' + $op) }
    $lines.Add('}')
    $lines.Add('')
    $inventory += [PSCustomObject]@{Class=$name;Abstract=$isAbstract;Source=("src/modelo/"+$name+".java");Attributes=$attrs;Operations=$ops}
}
@(
'Pessoa <|-- Aluno',
'Pessoa <|-- Treinador',
'',
"' Associacoes conceituais persistidas por IDs: nao sao referencias Java.",
"' Sem setas: navegabilidade de objetos nao especificada.",
'Aluno "1" -- "0..*" Matricula : idAluno',
'Treinador "1" -- "0..*" Turma : idTreinador',
'Turma "1" -- "0..*" Matricula : idTurma',
'Matricula "1" -- "0..*" Pagamento : idMatricula',
'',
'note bottom of Pagamento',
'  Associações persistidas por IDs inteiros.',
'  Multiplicidades válidas para registros persistidos.',
'  Navegabilidade de objetos não especificada.',
'end note',
'@enduml'
) | ForEach-Object { $lines.Add($_) }
[IO.File]::WriteAllLines((Join-Path $PSScriptRoot 'dominio.puml'), $lines, $utf8)
[IO.File]::WriteAllText((Join-Path $PSScriptRoot 'inventario-dominio.json'), ($inventory | ConvertTo-Json -Depth 5), $utf8)
Write-Output 'PlantUML e inventario gerados a partir das seis classes reais.'

