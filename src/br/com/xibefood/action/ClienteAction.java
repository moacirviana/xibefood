package br.com.xibefood.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import com.opensymphony.xwork2.ActionSupport;

import br.com.xibefood.DAO.ClienteDAOImpl;
import br.com.xibefood.dominio.BeanResult;
import br.com.xibefood.dominio.Cliente;
import br.com.xibefood.dominio.Usuario;

@SuppressWarnings("serial")
@Namespace("/clientes")
@ResultPath(value = "/")
@ParentPackage(value = "default")
public class ClienteAction extends ActionSupport {
	private List<Cliente> lstCliente;
	private Cliente cliente;
	private int id;
	private BeanResult result;
	private String flag;
	
	
	@Action(value = "listarJson", results = {
			@Result(name = "success", type = "json", params = { "root", "lstCliente" }),
			@Result(name = "error", location = "/pages/resultAjax.jsp") })
	public String doListarJson() {

		try {
			this.lstCliente = ClienteDAOImpl.getInstance().listar();
		} catch (Exception e) {
			addActionError(getText("listar.error"));
			return "error";
		}
		return "success";
	}
	
	@Action(value = "listarCbxJson", results = {
			@Result(name = "success", type = "json", params = { "root", "lstCliente" }),
			@Result(name = "error", location = "/pages/resultAjax.jsp") })
	public String doListarPorStatus() {

		try {
			this.lstCliente = ClienteDAOImpl.getInstance().listarCbx();
		} catch (Exception e) {
			addActionError(getText("listar.error"));
			return "error";
		}
		return "success";
	}
	
	
	// EXEMPLO DE NATIVE QUERY
	@Action(value = "listarSemComandaJson", results = {
			@Result(name = "success", type = "json", params = { "root", "lstCliente" }),
			@Result(name = "error", location = "/pages/resultAjax.jsp") })
	public String doListarSemComanda() {

		try {
			this.lstCliente = ClienteDAOImpl.getInstance().listarSemComanda();
		} catch (Exception e) {
			addActionError(getText("listar.error"));
			return "error";
		}
		return "success";
	}
	
	@Action(value = "listar", 
			results = { @Result(name = "success", location = "/consultas/cliente-listar.jsp"),
			@Result(name = "error", location = "/pages/error.jsp")},
			interceptorRefs = @InterceptorRef("authStack"))
	public String doListar() {

		try {
			this.lstCliente = ClienteDAOImpl.getInstance().listar();
		} catch (Exception e) {
			addActionError(getText("listar.error"));
			return "error";
		}
		return "success";
	}
	
	// * o m?todo alterar pode ser chamado para incluir um novo registro /*
	
	@Action(value = "alterar", results = { 
			@Result(name = "success", type = "json", params = { "root", "result" }),
			@Result(name = "error", location = "/pages/resultAjax.jsp")},
	        interceptorRefs = @InterceptorRef("authStack"))
	public String doAlterar() {
		HttpSession session = ServletActionContext.getRequest().getSession(true);
		Usuario b = (Usuario)session.getAttribute("login");
		int ret = 0;
		BeanResult res = new BeanResult();
		try {
			if (b.getAdmin()==1) {
				ret =  ClienteDAOImpl.getInstance().alterar(this.cliente);
				res.setMensagem(getText("alterar.sucesso"));
				res.setId(ret);
				
			}else {
			   res.setId(0);
			   res.setMensagem(getText("permissao.negada"));
			}
			this.result = res;
		} catch (Exception e) {
			   //addActionError(getText("editar.error"));
			    res.setMensagem(getText("alterar.error"));
			   res.setError(e.getMessage());
			 this.result = res;
			return "error";
		}
		return "success";
	}
	
	
	@Action(value = "inserir", results = { 
			@Result(name = "success", type = "json", params = { "root", "result" }),
			@Result(name = "error", location = "/pages/resultAjax.jsp")},
          	interceptorRefs = @InterceptorRef("authStack"))
	public String doInserir() {
		HttpSession session = ServletActionContext.getRequest().getSession(true);
		Usuario b = (Usuario)session.getAttribute("login");
		int ret = 0;
		BeanResult res = new BeanResult();
		try {
			if (b.getAdmin()==1) {
				// VERIFICA SE A DESCRI??O J? EXISTE
				Cliente c = new Cliente();
				c = ClienteDAOImpl.getInstance().getBeanByName(this.cliente.getNome());
				if (c.getNome()!=null) {
					res.setMensagem(getText("registro.existe"));
				}else {
					ret =  ClienteDAOImpl.getInstance().inserir(this.cliente);
					res.setMensagem(getText("inserir.sucesso"));
					res.setId(ret);
				}
				
			}else {
			   res.setId(0);
			   res.setMensagem(getText("permissao.negada"));
			}
			this.result = res;
		} catch (Exception e) {
			   //addActionError(getText("editar.error"));
			    res.setMensagem(getText("inserir.error"));
			   res.setError(e.getMessage());
			 this.result = res;
			return "error";
		}
		return "success";
	}
	
	
	@Action(value = "remover", results = { @Result(name = "success", type = "json", params = { "root", "result" }),
			@Result(name = "error", location = "/pages/error.jsp") })
	// interceptorRefs = @InterceptorRef("authStack")
	public String remover() {
		int ret = 0;
		BeanResult res = new BeanResult();
		try {
			HttpSession session = ServletActionContext.getRequest().getSession(true);
			Usuario b = (Usuario)session.getAttribute("login");
			if (b.getAdmin()==1) {
				ret =  ClienteDAOImpl.getInstance().remover(this.cliente);
				res.setMensagem("Cliente exclu?do com sucesso");
				res.setId(ret);
				this.result = res;
			}else {
				res.setId(0);
				res.setMensagem(getText("permissao.negada"));
			}
		} catch (Exception e) {
			   addActionError(getText("remover.error"));
			    res.setMensagem("Erro ao remover cliente");
			   res.setError(e.getMessage());
			 this.result = res;
			return "error";
		}
		return "success";
	}
	
	
	@Action(value = "frmSetupNovo", results = { @Result(name = "success", location = "/forms/frmCliente.jsp"),
			@Result(name = "error", location = "/pages/error.jsp")},
			interceptorRefs = @InterceptorRef("authStack"))
	public String frmSetupNovo() {
		try {
			this.flag = "inserir";
		} catch (Exception e) {
			addActionError(getText("frmsetup.error"));
			return "error";
		}
		return "success";
	}
	
	@Action(value = "frmSetupEditar", results = { @Result(name = "success", location = "/forms/frmCliente.jsp"),
			@Result(name = "error", location = "/pages/error.jsp")} 
	)			// interceptorRefs = @InterceptorRef("authStack")
	public String frmSetupEditar() {
		try {
			this.flag = "alterar";
			this.cliente = ClienteDAOImpl.getInstance().getBean(this.id);
		} catch (Exception e) {
			addActionError(getText("frmsetup.error"));
			return "error";
		}
		return "success";
	}
	

	@Action(value = "getBeanJson", results = { @Result(name = "success", type = "json", params = { "root", "produto" }),
			@Result(name = "error", location = "/pages/error.jsp") })
	public String getBeanJson() {
		try {
			this.cliente = ClienteDAOImpl.getInstance().getBean(this.id);
		} catch (Exception e) {
			addActionError(getText("getbean.error"));
			return "error";
		}
		return "success";
	}


	public List<Cliente> getLstCliente() {
		return lstCliente;
	}

	public void setLstCliente(List<Cliente> lstCliente) {
		this.lstCliente = lstCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BeanResult getResult() {
		return result;
	}

	public void setResult(BeanResult result) {
		this.result = result;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	

}