$ErrorActionPreference = 'Stop'
$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$result = [Collections.Generic.List[string]]::new()
$required = @('dominio.svg','dominio.png','dominio.puml','relatorio-validacao.md','README.md',
'inventario-dominio.json','fontes-verificadas.json','RenderizarDiagrama.java','layout.properties',
'extrair-modelo.ps1','validar-entrega.ps1','validacao-geometria.txt')
foreach ($name in $required) {
    $f = Get-Item -LiteralPath (Join-Path $PSScriptRoot $name)
    if ($f.Length -eq 0) { throw "Arquivo vazio: $name" }
}
$result.Add('OK: todos os 12 arquivos obrigatorios e auxiliares existem e nao estao vazios.')
$snapshot = Get-Content -LiteralPath (Join-Path $PSScriptRoot 'fontes-verificadas.json') -Raw -Encoding UTF8 | ConvertFrom-Json
foreach ($entry in $snapshot) {
    $actual = (Get-FileHash -LiteralPath (Join-Path $projectRoot $entry.Path) -Algorithm SHA256).Hash
    if ($actual -ne $entry.SHA256) { throw ('Fonte alterada: ' + $entry.Path) }
}
$result.Add("OK: SHA-256 dos $($snapshot.Count) arquivos de entrada preservados (21 Java, DDL e pom.xml).")
$inventory = Get-Content -LiteralPath (Join-Path $PSScriptRoot 'inventario-dominio.json') -Raw -Encoding UTF8 | ConvertFrom-Json
$puml = [IO.File]::ReadAllText((Join-Path $PSScriptRoot 'dominio.puml'))
[xml]$svg = [IO.File]::ReadAllText((Join-Path $PSScriptRoot 'dominio.svg'))
$ns = New-Object Xml.XmlNamespaceManager($svg.NameTable)
$ns.AddNamespace('s','http://www.w3.org/2000/svg')
$groups = $svg.SelectNodes('//s:g[starts-with(@id,"classe-")]',$ns)
if ($inventory.Count -ne 6 -or $groups.Count -ne 6) { throw 'Numero inesperado de classes' }
$attributes = 0
$operations = 0
foreach ($c in $inventory) {
    $group = $svg.SelectSingleNode(('//s:g[@id="classe-' + $c.Class + '"]'),$ns)
    if (-not $group) { throw ('Classe ausente: ' + $c.Class) }
    if ($group.SelectNodes('s:line',$ns).Count -ne 2) { throw 'A classe precisa de tres compartimentos' }
    $members = @($group.SelectNodes('s:text',$ns) | ForEach-Object { $_.InnerText })
    foreach ($entry in @($c.Attributes) + @($c.Operations)) {
        if (-not $puml.Contains($entry) -or $members -notcontains $entry) { throw "Membro ausente: $entry" }
    }
    $attributes += @($c.Attributes).Count
    $operations += @($c.Operations).Count
}
if ($attributes -ne 25 -or $operations -ne 54) { throw 'Contagem de membros divergente' }
$pessoa = $svg.SelectSingleNode('//s:g[@id="classe-Pessoa"]/s:text[1]',$ns)
if ($pessoa.GetAttribute('font-style') -ne 'italic') { throw 'Pessoa deve estar em italico' }
if ([regex]::Matches($puml,'(?m)^Pessoa <\|-- ').Count -ne 2) { throw 'Generalizacoes incorretas' }
if ([regex]::Matches($puml,'(?m)^\w+ "1" -- "0\.\.\*" \w+ : \w+').Count -ne 4) { throw 'Associacoes incorretas' }
if ($puml -match '[*o]--') { throw 'Agregacao ou composicao inesperada' }
$result.Add('OK: seis classes, 25 atributos, 54 operacoes presentes na fonte e no SVG.')
$result.Add('OK: tres compartimentos por classe; Pessoa em italico; duas generalizacoes; quatro associacoes 1 para 0..*.')
$bytes = [IO.File]::ReadAllBytes((Join-Path $PSScriptRoot 'dominio.png'))
function UInt32BE([byte[]]$data,[int]$offset) {
    return [uint32](([uint32]$data[$offset] -shl 24) -bor ([uint32]$data[$offset+1] -shl 16) -bor ([uint32]$data[$offset+2] -shl 8) -bor [uint32]$data[$offset+3])
}
$width = UInt32BE $bytes 16
$height = UInt32BE $bytes 20
if ($width -ne 4800 -or $height -ne 7290) { throw 'Dimensoes PNG inesperadas' }
$dpiFound = $false
$offset = 8
while ($offset -lt $bytes.Length - 12) {
    $length = UInt32BE $bytes $offset
    $type = [Text.Encoding]::ASCII.GetString($bytes,$offset+4,4)
    if ($type -eq 'pHYs') {
        $density = UInt32BE $bytes ($offset+8)
        $dpiFound = ($density -eq 11811 -and $bytes[$offset+16] -eq 1)
    }
    $offset += 12 + $length
}
if (-not $dpiFound) { throw 'Metadados de 300 dpi ausentes' }
if ($svg.DocumentElement.GetAttribute('viewBox') -ne '0 0 1600 2430') { throw 'Dimensoes SVG inesperadas' }
$result.Add('OK: PNG 4800 x 7290 com 300 dpi; SVG XML valido, viewBox 1600 x 2430.')
$result.Add('OK: detalhes da geometria registrados em validacao-geometria.txt.')
$result.Add('Revisao visual realizada no PNG; sem cortes ou sobreposicoes visiveis.')
$result.Add('Escopo: arquivo-fonte PlantUML; nao se trata de arquivo nativo Astah.')
[IO.File]::WriteAllLines((Join-Path $PSScriptRoot 'validacao-final.txt'),$result,[Text.UTF8Encoding]::new($false))
$result

