package curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @Autowired
    private ReportUtil reportUtil;
    
    static final int PG = 4;

    @RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
    public ModelAndView inicio() {

	ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
//	Iterable<Pessoa> pessoasIterable = pessoaRepository.findAllByOrderByIdAsc();
//	modelAndView.addObject("pessoaList", pessoasIterable);
	modelAndView.addObject("pessoaList", pessoaRepository
		.findAll(PageRequest.of(0, PG, Sort.by("nome").ascending().and(Sort.by("idade").descending()))));
	modelAndView.addObject("pessoaobj", new Pessoa());
	modelAndView.addObject("profissaoList", profissaoRepository.findAll());
	
	return modelAndView;
    }
    
    @GetMapping("/pessoaspag")
    public ModelAndView carregaPessoasPorPaginacao(@PageableDefault(size = PG) Pageable pageable,
	    ModelAndView modelAndView, @RequestParam("pesquisanome") String pesquisanome) {

//	Page<Pessoa> pagePessoa = pessoaRepository.findAll(pageable);
	Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(pesquisanome, pageable);
	modelAndView.addObject("pessoaList", pagePessoa);
	modelAndView.addObject("pessoaobj", new Pessoa());
	modelAndView.addObject("pesquisanome", pesquisanome);
	modelAndView.setViewName("cadastro/cadastropessoa");
	return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", consumes = { "multipart/form-data" })
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file)
	    throws IOException {
	System.out.println(file.getContentType());
	System.out.println(file.getName());
	System.out.println(file.getOriginalFilename());
	/**
	 * image/jpeg
	 * file
	 * imagem_teste.jpg
	 * application/pdf
	 * file
	 * Aula PL-SQL - parte 1 - Com respostas.pdf
	 */

	pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));

	String view = "cadastro/cadastropessoa";

	// verifica as validações
	if (bindingResult.hasErrors()) {
	    ModelAndView modelAndView = new ModelAndView(view);
//	    Iterable<Pessoa> pessoasIterable = pessoaRepository.findAllByOrderByIdAsc();
//	    modelAndView.addObject("pessoaList", pessoasIterable);
	    modelAndView.addObject("pessoaList", pessoaRepository
		    .findAll(PageRequest.of(0, PG, Sort.by("nome").ascending().and(Sort.by("idade").descending()))));
	    modelAndView.addObject("pessoaobj", pessoa);
	    modelAndView.addObject("profissaoList", profissaoRepository.findAll());

	    List<String> msg = new ArrayList<String>();
	    for (ObjectError objectError : bindingResult.getAllErrors()) {
		msg.add(objectError.getDefaultMessage()); // vem das anotações @NotEmpty, @NotNull e outras
	    }

	    modelAndView.addObject("msg", msg);

	    return modelAndView;
	}

	/**
	 * Rotina abaixo server para gravar currículo ou manter arquivo atual quando
	 * editar/atualizar
	 **/
	if (file.getSize() > 0) { /* Cadastrando um currículo */
	    pessoa.setCurriculo(file.getBytes());
	    pessoa.setTipoFileCurriculo(file.getContentType());
	    pessoa.setNomeFileCurriculo(file.getOriginalFilename());
	} else { /* Quando não informei currículo na tela e já existe um CV cadastrado */
	    if (pessoa.getId() != null && pessoa.getId() > 0) { // quando editando pessoa
		
		/**
		 * Linha de código anterior a implementação do cadastro de extensão
		 * e nome de arquivo quando upload.
		 * byte[] curriculoAtual = pessoaRepository.findById(pessoa.getId()).get().getCurriculo();
		 * pessoa.setCurriculo(curriculoAtual);
		 * pessoa.setTipoFileCurriculo(file.getContentType());
		 * pessoa.setNomeFileCurriculo(file.getOriginalFilename());
		 **/
		Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
		pessoa.setCurriculo(pessoaTemp.getCurriculo());
		pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
		pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
	    }
	}

	pessoaRepository.save(pessoa);

	ModelAndView modelAndView = new ModelAndView(view);
	// pessoas = (List<Pessoa>) pessoaRepository.findAll();
	// List<Pessoa> pessoas = (List<Pessoa>) pessoaRepository.findAll();
//	Iterable<Pessoa> pessoasIterable = pessoaRepository.findAll();
//	modelAndView.addObject("pessoaList", pessoasIterable);
	modelAndView.addObject("pessoaList", pessoaRepository
		    .findAll(PageRequest.of(0, PG, Sort.by("nome").ascending().and(Sort.by("idade").descending()))));
	modelAndView.addObject("pessoaobj", new Pessoa());
	modelAndView.addObject("profissaoList", profissaoRepository.findAll());

//		return "cadastro/cadastropessoa";
	return modelAndView;
    }
    
    @GetMapping("**/downloadcv/{idpessoa}")
    public void downloadCV(@PathVariable("idpessoa") Long idpessoa, HttpServletResponse response) throws IOException {
	/**
	 * 1º - Consultar o objeto pessoa no banco de dados.
	 **/
	Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
	if (pessoa.getCurriculo() != null) {
	    /* Capturar tamanho da resposta */
	    response.setContentLength(pessoa.getCurriculo().length);
	    /*
	     * Tipo do arquivo para download ou pode ser generica (application/octet-stream)
	     */
	    response.setContentType(pessoa.getTipoFileCurriculo());
	    /* Define o cabeçalho da resposta */
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment;filename=\"%s\"", pessoa.getNomeFileCurriculo());
	    response.setHeader(headerKey, headerValue);
	    /* Finaliza a resposta passando o arquivo */
	    response.getOutputStream().write(pessoa.getCurriculo());
	}
    }

    @RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
    public ModelAndView pessoas() {

	ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
//	Iterable<Pessoa> pessoasIterable = pessoaRepository.findAllByOrderByIdAsc();
//	modelAndView.addObject("pessoaList", pessoasIterable);
	modelAndView.addObject("pessoaList", pessoaRepository
		    .findAll(PageRequest.of(0, PG, Sort.by("nome").ascending().and(Sort.by("idade").descending()))));
	modelAndView.addObject("pessoaobj", new Pessoa());
	modelAndView.addObject("profissaoList", profissaoRepository.findAll());

	return modelAndView;
    }

    // @RequestMapping(method = RequestMethod.GET, value = editarpessoa/{idpessoa}")
    @GetMapping("/editarpessoa/{idpessoa}")
    public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

	Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
	ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
	modelAndView.addObject("pessoaobj", pessoa.get());
	modelAndView.addObject("profissaoList", profissaoRepository.findAll());

	return modelAndView;
    }

    @GetMapping("/removerpessoa/{idpessoa}")
    public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

	pessoaRepository.deleteById(idpessoa);

	ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
//	modelAndView.addObject("pessoaList", pessoaRepository.findAllByOrderByIdAsc());
	modelAndView.addObject("pessoaList", pessoaRepository
		    .findAll(PageRequest.of(0, PG, Sort.by("nome").ascending().and(Sort.by("idade").descending()))));
	modelAndView.addObject("pessoaobj", new Pessoa());

	return modelAndView;
    }

    @GetMapping("**/pesquisarpessoas")
    public void imprimirPdf(@RequestParam("pesquisanome") String pesquisanome,
	    @RequestParam("pesquisasexo") Character pesquisasexo, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// System.out.println("Chamou o método imprimirPdf");

	List<Pessoa> pessoas = new ArrayList<Pessoa>();

	if (pesquisasexo != null && !pesquisasexo.toString().isEmpty() && pesquisanome != null
		&& !pesquisanome.isEmpty()) {

	    pessoas = pessoaRepository.findPessoaByNameSexo(pesquisanome, pesquisasexo);

	} else if (pesquisanome != null && !pesquisanome.isEmpty()) {

	    pessoas = pessoaRepository.findPessoaByName(pesquisanome);

	} else if (pesquisasexo != null && !pesquisasexo.toString().isEmpty()) {

	    pessoas = pessoaRepository.findPessoaBySexo(pesquisasexo);

	} else {

	    pessoas = (List<Pessoa>) pessoaRepository.findAll();
//			Iterable<Pessoa> iterable = pessoaRepository.findAll();
//			for (Pessoa pessoa : iterable) {
//				pessoas.add(pessoa);
//			}
	}
	/* Chama o serviço que gera o relatório */ /*"pessoa" => pessoa.jasper*/
	byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
	/* Tamanho da resposta */
	response.setContentLength(pdf.length);
	/* Definir o tipo de arquivo */
	response.setContentType("application/octet-stream");
	/* Definir o cabeçalho da resposta */
	String headerKey = "Content-Disposition";
	String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
	response.setHeader(headerKey, headerValue);
	/* Finaliza a resposta para o navegador */
	response.getOutputStream().write(pdf);
    }

    @PostMapping("**/pesquisarpessoas")
    public ModelAndView pesquisar(@RequestParam("pesquisanome") String pesquisanome,
	    @RequestParam("pesquisasexo") Character pesquisasexo,
	    @PageableDefault(size = PG, sort = { "nome" }) Pageable pageable) {

//	List<Pessoa> pessoas = new ArrayList<Pessoa>();
	Page<Pessoa> pessoas = null;

	if (pesquisasexo != null && !pesquisasexo.toString().isEmpty() && pesquisanome != null
		&& !pesquisanome.isEmpty()) {
//	    pessoas = pessoaRepository.findPessoaByNameSexo(pesquisanome, pesquisasexo);
//	    pessoas = pessoaRepository.findByNomeAndSexoIgnoreCase(pesquisanome, pesquisasexo);
	    pessoas = pessoaRepository.findPessoaByNameSexoPage(pesquisanome, pesquisasexo, pageable);
	} else if (pesquisanome != null && !pesquisanome.isEmpty()) {
//	    pessoas = pessoaRepository.findPessoaByName(pesquisanome);
//	    pessoas = pessoaRepository.findByNomeIgnoreCase(pesquisanome);
	    pessoas = pessoaRepository.findPessoaByNamePage(pesquisanome, pageable);
	} else if (pesquisasexo != null && !pesquisasexo.toString().isEmpty()) {
	    pessoas = pessoaRepository.findPessoaBySexoPage(pesquisasexo, pageable);
	} else {
	    pessoas = pessoaRepository.findAll(null, pageable);
	    Iterable<Pessoa> iterable = pessoaRepository.findAll();
	    for (Pessoa pessoa : iterable) {
		pessoas.and(pessoa);
	    }
	}

	String view = "cadastro/cadastropessoa";
	ModelAndView modelAndView = new ModelAndView(view);
	modelAndView.addObject("pessoaList", pessoas);
	modelAndView.addObject("pessoaobj", new Pessoa());
	modelAndView.addObject("pesquisanome", pesquisanome);
	return modelAndView;
    }
    
    @GetMapping("/telefonespessoa/{idpessoa}")
    public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

	Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
	ModelAndView modelAndView = new ModelAndView("cadastro/telefonespessoa");
	modelAndView.addObject("pessoaobj", pessoa.get());
	modelAndView.addObject("telefoneList", telefoneRepository.getTelefones(idpessoa));

	return modelAndView;
    }

    @PostMapping("**/addfonePessoa/{idpessoa}")
    public ModelAndView addfonePessoa(Telefone telefone, @PathVariable("idpessoa") Long idpessoa) {

	Pessoa pessoa = pessoaRepository.findById(idpessoa).get();

	if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {

	    ModelAndView modelAndView = new ModelAndView("cadastro/telefonespessoa");
	    modelAndView.addObject("telefoneList", telefoneRepository.getTelefones(idpessoa));
	    modelAndView.addObject("pessoaobj", pessoa);

	    List<String> msg = new ArrayList<String>();
	    if (telefone.getNumero().isEmpty()) {
		msg.add("Número de telefone deve ser informado!");
	    }
	    if (telefone.getTipo().isEmpty()) {
		msg.add("Tipo de telefone deve ser informado!");
	    }
	    modelAndView.addObject("msg", msg);

	    return modelAndView;
	}

	telefone.setPessoa(pessoa);

	telefoneRepository.save(telefone);

	ModelAndView modelAndView = new ModelAndView("cadastro/telefonespessoa");
	modelAndView.addObject("telefoneList", telefoneRepository.getTelefones(idpessoa));
	modelAndView.addObject("pessoaobj", pessoa);

	return modelAndView;
    }

    @GetMapping("/removertelefone/{idtelefone}")
    public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

	Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

	telefoneRepository.deleteById(idtelefone);

	ModelAndView modelAndView = new ModelAndView("cadastro/telefonespessoa");
	modelAndView.addObject("pessoaobj", pessoa);
	modelAndView.addObject("telefoneList", telefoneRepository.getTelefones(pessoa.getId()));

	return modelAndView;
    }

}
