package curso.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	// Retorna PDF em Byte[] para download no navegador.
	public byte[] gerarRelatorio(List<?> listDados, String relatorio, ServletContext servletContext) throws Exception {

		/* cria lista de dados para o relatorio com lista de objetos para imprimir */
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(listDados);
		/* carrega o caminho do arquivo jasper compilado */
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
		/* Carrega o arquivo jasper passando os dados */
		Map<String, Object> params = null;
		JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoJasper, params, source);
		/* Exporta para byte[] para fazer o download do PDF */
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

}
