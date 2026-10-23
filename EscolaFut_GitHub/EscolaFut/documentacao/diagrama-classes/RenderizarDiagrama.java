import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;

/**
 * Renderizador local do subconjunto PlantUML usado neste diagrama.
 * Nao e o programa PlantUML nem uma implementacao do formato Astah.
 * Le classes, membros, generalizacoes, associacoes e nota do arquivo .puml.
 * Posicionamento manual editavel em layout.properties.
 */
public class RenderizarDiagrama {
    record Classe(String nome, boolean abstrata, List<String> atributos,
                  List<String> operacoes, double x, double y, double w, double h) {
        double cx() { return x + w / 2; }
        double bottom() { return y + h; }
        Rectangle2D bounds() { return new Rectangle2D.Double(x,y,w,h); }
    }
    record Relacao(String a, String b, String ma, String mb, String rotulo) {}
    record Segmento(double x1, double y1, double x2, double y2) {}
    static final Map<String,Classe> classes = new LinkedHashMap<>();
    static final List<Segmento> segmentos = new ArrayList<>();
    static final List<String> verificacoes = new ArrayList<>();
    static final StringBuilder svg = new StringBuilder();
    static final Font TEXTO = new Font("Arial", Font.PLAIN, 20);
    static final Font NOME = new Font("Arial", Font.BOLD, 23);
    static Graphics2D g;
    static int largura, altura;
    static double escala;
    static int textos = 0;
    static String xml(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }
    static void texto(String s, double x, double y, Font f) {
        FontMetrics metrics = g.getFontMetrics(f);
        if (x < 0 || x + metrics.stringWidth(s) > largura - 4 || y > altura - 4)
            throw new IllegalStateException("Texto fora da imagem: " + s);
        g.setColor(Color.BLACK); g.setFont(f); g.drawString(s,(float)x,(float)y);
        svg.append("<text x=\"").append(x).append("\" y=\"").append(y)
            .append("\" font-family=\"Arial,Helvetica,sans-serif\" font-size=\"")
            .append(f.getSize()).append("\"");
        if(f.isBold()) svg.append(" font-weight=\"bold\"");
        if(f.isItalic()) svg.append(" font-style=\"italic\"");
        svg.append(">").append(xml(s)).append("</text>\n");
        textos++;
    }
    static void central(String s, double cx, double y, Font f) {
        texto(s,cx-g.getFontMetrics(f).stringWidth(s)/2.0,y,f);
    }
    static void linha(double x1,double y1,double x2,double y2,boolean relacao) {
        g.setColor(new Color(34,34,34)); g.setStroke(new BasicStroke(1.4f));
        g.draw(new Line2D.Double(x1,y1,x2,y2));
        svg.append("<line x1=\"").append(x1).append("\" y1=\"").append(y1)
            .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2)
            .append("\" stroke=\"#222\" stroke-width=\"1.4\"/>\n");
        if (relacao) segmentos.add(new Segmento(x1,y1,x2,y2));
    }
    static void retangulo(double x,double y,double w,double h) {
        g.setColor(Color.WHITE); g.fill(new Rectangle2D.Double(x,y,w,h));
        g.setColor(new Color(34,34,34)); g.setStroke(new BasicStroke(1.4f));
        g.draw(new Rectangle2D.Double(x,y,w,h));
        svg.append("<rect x=\"").append(x).append("\" y=\"").append(y)
            .append("\" width=\"").append(w).append("\" height=\"").append(h)
            .append("\" fill=\"white\" stroke=\"#222\" stroke-width=\"1.4\"/>\n");
    }
    static void desenharClasse(Classe c) {
        svg.append("<g id=\"classe-").append(c.nome).append("\">\n");
        retangulo(c.x,c.y,c.w,c.h);
        linha(c.x,c.y+46,c.x+c.w,c.y+46,false);
        double sep = c.y+46+24+c.atributos.size()*26;
        linha(c.x,sep,c.x+c.w,sep,false);
        central(c.nome,c.cx(),c.y+31,c.abstrata?NOME.deriveFont(Font.BOLD|Font.ITALIC):NOME);
        double y = c.y+46+27;
        for(String s:c.atributos) { membro(c,s,y); y+=26; }
        y=sep+29;
        for(String s:c.operacoes) { membro(c,s,y); y+=26; }
        svg.append("</g>\n");
    }
    static void membro(Classe c,String s,double y) {
        if(g.getFontMetrics(TEXTO).stringWidth(s)>c.w-28)
            throw new IllegalStateException("Membro excede largura de "+c.nome+": "+s);
        texto(s,c.x+14,y,TEXTO);
    }
    static void herancas(String src) {
        Matcher m=Pattern.compile("(?m)^(\\w+) <\\|-- (\\w+)\\s*$").matcher(src);
        Map<String,List<Classe>> filhos=new LinkedHashMap<>();
        while(m.find()) filhos.computeIfAbsent(m.group(1),k->new ArrayList<>()).add(classes.get(m.group(2)));
        for(var e:filhos.entrySet()) {
            Classe pai=classes.get(e.getKey());
            double topoFilhos=e.getValue().stream().mapToDouble(c->c.y).min().orElseThrow();
            double bus=(pai.bottom()+topoFilhos)/2;
            for(Classe filho:e.getValue()) {
                linha(filho.cx(),filho.y,filho.cx(),bus,true);
                linha(filho.cx(),bus,pai.cx(),bus,true);
            }
            linha(pai.cx(),bus,pai.cx(),pai.bottom()+20,true);
            Path2D p=new Path2D.Double();
            p.moveTo(pai.cx(),pai.bottom());p.lineTo(pai.cx()-11,pai.bottom()+20);
            p.lineTo(pai.cx()+11,pai.bottom()+20);p.closePath();
            g.setColor(Color.WHITE);g.fill(p);g.setColor(new Color(34,34,34));g.draw(p);
            svg.append("<polygon points=\"").append(pai.cx()).append(",").append(pai.bottom())
                .append(" ").append(pai.cx()-11).append(",").append(pai.bottom()+20)
                .append(" ").append(pai.cx()+11).append(",").append(pai.bottom()+20)
                .append("\" fill=\"white\" stroke=\"#222\" stroke-width=\"1.4\"/>\n");
        }
    }
    static List<Relacao> associacoes(String src) {
        List<Relacao> lista=new ArrayList<>();
        Matcher m=Pattern.compile("(?m)^(\\w+) \"([^\"]+)\" -- \"([^\"]+)\" (\\w+) : (\\w+)\\s*$").matcher(src);
        while(m.find())lista.add(new Relacao(m.group(1),m.group(4),m.group(2),m.group(3),m.group(5)));
        for(Relacao r:lista) {
            Classe a=classes.get(r.a), b=classes.get(r.b);
            if(a==null||b==null)throw new IllegalArgumentException("Classe inexistente");
            if(Math.abs(a.y-b.y)<1) {
                Classe esq=a.x<b.x?a:b, dir=a.x<b.x?b:a;
                String me=esq==a?r.ma:r.mb, md=dir==a?r.ma:r.mb;
                double y=Math.min(a.y+a.h/2,b.y+b.h/2);
                linha(esq.x+esq.w,y,dir.x,y,true);
                central(r.rotulo,(esq.x+esq.w+dir.x)/2,y-16,TEXTO);
                texto(me,esq.x+esq.w+10,y+26,TEXTO);
                texto(md,dir.x-10-g.getFontMetrics(TEXTO).stringWidth(md),y+26,TEXTO);
            } else {
                Classe cima=a.y<b.y?a:b, baixo=a.y<b.y?b:a;
                String mc=cima==a?r.ma:r.mb, mb=baixo==a?r.ma:r.mb;
                if(Math.abs(cima.cx()-baixo.cx())>1)throw new IllegalStateException("Alinhe os centros para associacao vertical");
                linha(cima.cx(),cima.bottom(),baixo.cx(),baixo.y,true);
                texto(mc,cima.cx()+12,cima.bottom()+25,TEXTO);
                texto(mb,baixo.cx()+12,baixo.y-12,TEXTO);
                texto(r.rotulo,cima.cx()+18,(cima.bottom()+baixo.y)/2+6,TEXTO);
            }
        }
        return lista;
    }
    static void validarGeometria() {
        List<Classe> lista=new ArrayList<>(classes.values());
        for(int i=0;i<lista.size();i++)for(int j=i+1;j<lista.size();j++)
            if(lista.get(i).bounds().intersects(lista.get(j).bounds()))
                throw new IllegalStateException("Classes sobrepostas");
        for(Segmento s:segmentos)for(Classe c:lista) {
            Rectangle2D interior=new Rectangle2D.Double(c.x+2,c.y+2,c.w-4,c.h-4);
            if(interior.intersectsLine(s.x1,s.y1,s.x2,s.y2))
                throw new IllegalStateException("Linha atravessa classe "+c.nome);
        }
        verificacoes.add("Sem sobreposicao entre classes; nenhuma linha de relacionamento atravessa caixas.");
        verificacoes.add("Largura de todos os membros verificada com FontMetrics; nenhum texto cortado.");
    }
    static void nota(String src,Properties p) {
        Matcher m=Pattern.compile("(?s)note bottom of \\w+\\R(.*?)\\Rend note").matcher(src);
        if(!m.find())return;
        List<String> frases=m.group(1).lines().map(String::trim).toList();
        double x=Double.parseDouble(p.getProperty("nota.x","900"));
        double y=Double.parseDouble(p.getProperty("nota.y","1900"));
        texto("Nota de modelagem",x,y,NOME.deriveFont(21f));
        int i=0;
        for(String frase:frases) {
            // Quebra apenas para apresentacao; preserva o texto da fonte.
            String[] palavras=frase.split(" ");String atual="";
            for(String palavra:palavras){
                String teste=atual.isEmpty()?palavra:atual+" "+palavra;
                if(g.getFontMetrics(TEXTO).stringWidth(teste)>620){
                    texto(atual,x,y+38+i++*28,TEXTO);atual=palavra;
                }else atual=teste;
            }
            if(!atual.isEmpty())texto(atual,x,y+38+i++*28,TEXTO);
            i++;
        }
    }
    static void png(BufferedImage img,Path path)throws Exception {
        ImageWriter writer=ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam param=writer.getDefaultWriteParam();
        IIOMetadata meta=writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(img),param);
        IIOMetadataNode root=new IIOMetadataNode("javax_imageio_png_1.0");
        IIOMetadataNode phys=new IIOMetadataNode("pHYs");
        phys.setAttribute("pixelsPerUnitXAxis","11811");phys.setAttribute("pixelsPerUnitYAxis","11811");
        phys.setAttribute("unitSpecifier","meter");root.appendChild(phys);
        meta.mergeTree("javax_imageio_png_1.0",root);
        try(ImageOutputStream out=ImageIO.createImageOutputStream(path.toFile())) {
            writer.setOutput(out);writer.write(null,new IIOImage(img,null,meta),param);
        }finally{writer.dispose();}
    }
    public static void main(String[] args)throws Exception {
        Path pasta=Path.of(args.length>0?args[0]:".").toAbsolutePath();
        String src=Files.readString(pasta.resolve("dominio.puml"),StandardCharsets.UTF_8);
        Properties p=new Properties();
        try(Reader r=Files.newBufferedReader(pasta.resolve("layout.properties"))){p.load(r);}
        largura=Integer.parseInt(p.getProperty("canvas.width","1600"));
        altura=Integer.parseInt(p.getProperty("canvas.height","2430"));
        escala=Double.parseDouble(p.getProperty("png.scale","3"));
        Matcher matcher=Pattern.compile("(?s)(abstract )?class (\\w+) \\{(.*?)\\R\\}").matcher(src);
        int attrs=0,ops=0;
        while(matcher.find()) {
            String nome=matcher.group(2);
            List<String> at=new ArrayList<>(),op=new ArrayList<>();boolean metodos=false;
            for(String line:matcher.group(3).split("\\R")){
                String s=line.trim();if(s.equals("--")){metodos=true;continue;}
                if(s.isEmpty())continue;
                if(metodos)op.add(s);else at.add(s);
            }
            String[] coords=p.getProperty(nome).split(",");
            double h=46+24+at.size()*26+24+op.size()*26;
            classes.put(nome,new Classe(nome,matcher.group(1)!=null,at,op,
                Double.parseDouble(coords[0]),Double.parseDouble(coords[1]),Double.parseDouble(coords[2]),h));
            attrs+=at.size();ops+=op.size();
        }
        BufferedImage img=new BufferedImage((int)(largura*escala),(int)(altura*escala),BufferedImage.TYPE_INT_RGB);
        g=img.createGraphics();g.scale(escala,escala);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);g.fillRect(0,0,largura,altura);
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(largura)
            .append("\" height=\"").append(altura).append("\" viewBox=\"0 0 ").append(largura).append(" ")
            .append(altura).append("\" role=\"img\" aria-labelledby=\"title desc\">\n")
            .append("<title id=\"title\">EscolaFut - Diagrama de classes do dominio</title>\n")
            .append("<desc id=\"desc\">Seis classes Java, atributos e operacoes declarados; duas generalizacoes e quatro associacoes persistidas por IDs.</desc>\n")
            .append("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>\n");
        central("EscolaFut — Diagrama de classes do domínio",largura/2.0,42,NOME.deriveFont(24f));
        herancas(src);
        List<Relacao> rels=associacoes(src);
        for(Classe c:classes.values())desenharClasse(c);
        nota(src,p);
        texto("Pacote modelo | Java + DDL SQL Server | + público  - privado  # protegido",60,altura-45,TEXTO.deriveFont(17f));
        validarGeometria();
        svg.append("</svg>\n");
        Files.writeString(pasta.resolve("dominio.svg"),svg.toString(),StandardCharsets.UTF_8);
        png(img,pasta.resolve("dominio.png"));g.dispose();
        verificacoes.add("Classes: "+classes.size()+"; atributos declarados: "+attrs+"; operacoes declaradas: "+ops+".");
        verificacoes.add("Generalizacoes: 2; associacoes: "+rels.size()+".");
        verificacoes.add("SVG: "+largura+" x "+altura+"; PNG: "+img.getWidth()+" x "+img.getHeight()+"; densidade: 300 dpi.");
        verificacoes.add("Elementos de texto SVG: "+textos+". SVG e PNG gerados da mesma leitura do PlantUML.");
        Files.write(pasta.resolve("validacao-geometria.txt"),verificacoes,StandardCharsets.UTF_8);
        verificacoes.forEach(System.out::println);
    }
}

