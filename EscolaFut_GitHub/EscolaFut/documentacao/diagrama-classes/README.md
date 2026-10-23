# Diagrama de classes — EscolaFut

Abra **dominio.svg** no navegador ou **dominio.png** em um visualizador de imagens.

| Arquivo | Finalidade |
|---|---|
| dominio.svg | Diagrama vetorial, com texto selecionável e escalável |
| dominio.png | Diagrama em 4800 × 7290 pixels a 300 dpi |
| dominio.puml | Fonte PlantUML editável, não nativa do Astah |
| relatorio-validacao.md | Evidências, multiplicidades, escopo e limitações |
| inventario-dominio.json | Todos os atributos e operações declarados no domínio |
| fontes-verificadas.json | Hashes das fontes analisadas |
| layout.properties | Coordenadas e dimensões para o desenho manual |
| RenderizarDiagrama.java | Renderizador local da sintaxe utilizada no .puml |
| extrair-modelo.ps1 | Extração reproduzível dos membros do modelo Java |
| validar-entrega.ps1 | Validação da integridade e dos arquivos de saída |
| validacao-geometria.txt | Verificação de caixas, linhas e texto |
| validacao-final.txt | Verificação de arquivos, fonte e integridade |

## Regenerar as imagens preservando o layout desta entrega

Na raiz do projeto, com um JDK 21 ou superior:

```powershell
java -Xmx768m '-Djava.awt.headless=true' documentacao\diagrama-classes\RenderizarDiagrama.java documentacao\diagrama-classes
```

O renderizador lê o arquivo PlantUML e usa `layout.properties`. Ele cobre o subconjunto usado nesta entrega, não a linguagem PlantUML inteira. Se adicionar classes, ajuste também o layout e revise as associações; construções adicionais de UML requerem extensão do renderizador ou uso do PlantUML oficial.

Para modificar apenas o desenho, edite `dominio.puml` e as coordenadas. Para atualizar atributos e operações a partir de mudanças no Java, execute o extrator abaixo; ele **substitui** o .puml pelas declarações atuais e pelas quatro associações deste modelo:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File documentacao\diagrama-classes\extrair-modelo.ps1
```

O parâmetro de política vale somente para esse processo, sem alterar permanentemente a configuração do Windows.

## Renderizar com o PlantUML oficial

Com o arquivo `plantuml.jar` obtido da [distribuição oficial](https://plantuml.com/download), na pasta deste diagrama:

```powershell
java -jar C:\caminho\plantuml.jar -charset UTF-8 -tsvg dominio.puml
java -jar C:\caminho\plantuml.jar -charset UTF-8 -tpng dominio.puml
```

O algoritmo automático pode produzir um layout diferente. Conforme a distribuição, o PlantUML pode precisar do Graphviz; a opção [Smetana](https://plantuml.com/smetana02) oferece um motor Java interno. Nenhum download é necessário para o renderizador local incluído.

## Validar

Na raiz do projeto:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File documentacao\diagrama-classes\validar-entrega.ps1
```

O snapshot corresponde às fontes em 21/10/2026. Alterações posteriores no sistema serão detectadas como divergências; isso é esperado quando o código evoluir. O script não atualiza o snapshot silenciosamente.

