# Abrir e executar no IntelliJ IDEA

O projeto usa Java 21 e Maven. A configuração Maven está no arquivo pom.xml; o driver JDBC do SQL Server já está em lib/.

1. No IntelliJ, escolha **File > Open** e selecione a pasta EscolaFut_SQLServer (a pasta que contém pom.xml). Abra como projeto Maven e aguarde a sincronização.
2. Abra **File > Project Structure > Project** e selecione um JDK 21 em **Project SDK**. O projeto define a versão de linguagem como 21.
3. Abra src/controle/MenuPrincipal.java. O método main está nessa classe. Clique no triângulo verde ao lado do método e escolha **Run 'MenuPrincipal.main()'**.
4. Configure as variáveis de ambiente ESCOLAFUT_SQLSERVER_USER e ESCOLAFUT_SQLSERVER_PASSWORD no Windows. Se sua instância SQL Server não usa localhost:1433, configure também ESCOLAFUT_SQLSERVER_URL. Depois reinicie o IntelliJ.
5. Confirme que a base EscolaFutebol já foi criada pelo script database/escola_futebol.sql.

Se o triângulo continuar ausente, confirme que o plugin Java está habilitado e que o IntelliJ abriu a pasta que contém pom.xml (não apenas a pasta src ou um arquivo .java). Em **Project Structure > Modules**, src deve aparecer como source root. Se o projeto já estava aberto antes da criação do pom.xml, use **File > Reload All Maven Projects** no painel Maven ou feche e abra a pasta novamente.

Para verificar a gravação, cadastre um aluno e execute no SSMS: SELECT * FROM dbo.Aluno ORDER BY id_aluno DESC;
