package dao;

import conexao.Conexao;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Pagamento;

public class PagamentoDAO {
    private final Conexao conexao;
    public PagamentoDAO(Conexao conexao){this.conexao=conexao;}
    public void inserir(Pagamento p)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("INSERT INTO pagamento(vencimento,data_pagamento,valor,situacao,id_matricula) VALUES (?,?,?,?,?)")){ps.setDate(1,Date.valueOf(p.getVencimento())); if(p.getDataPagamento()==null) ps.setNull(2,java.sql.Types.DATE); else ps.setDate(2,Date.valueOf(p.getDataPagamento())); ps.setBigDecimal(3,p.getValor());ps.setString(4,p.getSituacao());ps.setInt(5,p.getIdMatricula());ps.executeUpdate();}}
    public void atualizar(Pagamento p)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("UPDATE pagamento SET vencimento=?,data_pagamento=?,valor=?,situacao=?,id_matricula=? WHERE id_pagamento=?")){ps.setDate(1,Date.valueOf(p.getVencimento()));if(p.getDataPagamento()==null) ps.setNull(2,java.sql.Types.DATE); else ps.setDate(2,Date.valueOf(p.getDataPagamento()));ps.setBigDecimal(3,p.getValor());ps.setString(4,p.getSituacao());ps.setInt(5,p.getIdMatricula());ps.setInt(6,p.getIdPagamento());ps.executeUpdate();}}
    public void excluir(int id)throws SQLException{try(PreparedStatement ps=conexao.preparaSQL("DELETE FROM pagamento WHERE id_pagamento=?")){ps.setInt(1,id);ps.executeUpdate();}}
    public List<Pagamento> listar()throws SQLException{List<Pagamento> l=new ArrayList<>();try(PreparedStatement ps=conexao.preparaSQL("SELECT * FROM pagamento ORDER BY vencimento DESC");ResultSet rs=ps.executeQuery()){while(rs.next()){Pagamento p=new Pagamento();p.setIdPagamento(rs.getInt("id_pagamento"));p.setVencimento(rs.getDate("vencimento").toLocalDate());Date dp=rs.getDate("data_pagamento");p.setDataPagamento(dp==null?null:dp.toLocalDate());p.setValor(rs.getBigDecimal("valor"));p.setSituacao(rs.getString("situacao"));p.setIdMatricula(rs.getInt("id_matricula"));l.add(p);}}return l;}
}
