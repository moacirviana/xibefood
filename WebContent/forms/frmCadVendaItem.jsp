<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="mnuItemComanda">active</s:set>
<jsp:include page = "/mainhead.inc.jsp" />


<div class="container">
  <div class="card">
	<div class="card-body">
	   <h5 class="card-title">Add itens</h5>
   
 
	 <form action="incluirItem" method="post" name="form1" id="frmVenda">    
	          <div class="form-row">
			    <div class="form-group col-md-6">
			      <label for="inputNome">Cliente</label>
			          
			         <select class="form-control form-control-sm" id="cliente" name="venda.id">
					      <option value="4">Moacir Viana</option>
				    </select>
			      
			    </div>
			    <div class="form-group col-md-6">
			      <label for="inputCPF">Total</label>
			      <input name="venda.total" type="text" value="" class="form-control form-control-sm" id="tot" placeholder="R$">
			    </div>
			  </div>
			  
			  
			  
					  
			   <div class="form-row">
			    <table class="table">
					  <thead>
					    <tr>
					      <th scope="col">#</th>
					      <th scope="col">Produto</th>
					      <th scope="col">VlUnd</th>
					      <th scope="col">Qtd</th>
					      <th scope="col">Valor R$</th>
					      <th scope="col"><button id="add-row" type="button" class="btn btn-sm btn-info">Add Item</button></th>
					      
					    </tr>
					  </thead>
					  <tbody>
					    <tr id="rowid0">
					      <th scope="row">0</th>
					      <td> 
					        <select class="form-control form-control-sm" name="itens[0].id.produto.id" id="produto0" onChange="getValor(0,this.value)">
					          <option value="1" selected>1</option>
				          </select>
				          </td>					      
				          <td><input name="itens[0].valorunt" id="vlrund0" type="text" value="" size="7" class="form-control form-control-sm"></td>
					      <td><input name="itens[0].quantidade" id="qtd0" type="text" size="5" value="1" class="form-control form-control-sm"></td>
					      <td><input name="itens[0].valor" id="vlr0" type="text" size="5" value="" class="form-control form-control-sm"></td>
					      <td></td>
					      
					    </tr>
	    				
					  </tbody>
					</table>
			    
			    
			  </div>
			  				  
			   <button id="btnVenda" type="button" class="btn btn-sm btn-success">Salvar</button>
			   
       </form>
       <br>
       <br>
		<s:if test="hasActionErrors()">
		   <div class="alert alert-danger" role="alert" id="success_message"> <i class="glyphicon glyphicon-thumbs-down"></i>
				<s:actionerror/>
			</div>	
		</s:if>
		
		<s:if test="hasActionMessages()">
			<!-- Success message -->
	       <div class="alert alert-success" role="alert" id="success_message">
	   		     <s:actionmessage/>
		    </div>
		</s:if>

   </div>  
 </div>
</div>		

  
<jsp:include page = "/javascripts.jsp" />
<script type="text/javascript">

$(document).ready(function() {
	function CarregaCliente(){
	    var select = $('#cliente');	      
	    select.find('option').remove();	      
			 
	    	$.getJSON('../comandas/listarSituacaoJson',function(jsonResponse) {			    	  
			    	  $('<option>').val(-1).text("Informe o Cliente").appendTo(select);
			    	      var status="";
			              $.each(jsonResponse, function(key, value) {
			            	  if (value.status==1){status=" (j?? pediu a conta)";}
			            	  else{status="";}
			                 $('<option>').val(value.id).text(value.cliente+status).appendTo(select);
	       			 });
		     }); 
	  }

	function CarregaProduto(id){
	  var select = $('#produto'+id);	      
	      select.find('option').remove();	      
			  
	      $.getJSON('../produtos/listarJson',function(jsonResponse) {			    	  
			    	  $('<option>').val(-1).text("Informe o card??pio").appendTo(select);			    	  
			              $.each(jsonResponse, function(key, value) {
			                $('<option>').val(value.id).text(("0000" + value.id).slice(-4) + " " + value.descricao).appendTo(select);
	       			      });
		          });
			
	  }	
	
	var line = 0;
 
	 $("#add-row").click(function(){
		 line++;
         var markup ='<tr><th scope="row">'+line+'</th>'+
				      '<td>'+ 
				        '<select class="form-control form-control-sm" onChange="getValor('+line+',this.value)" name="itens['+line+'].id.produto.id" id="produto'+line+'">'+
				                '<option value="1">1</option>'+
			            '</select>'+
			          '</td>'+					      
			          '<td><input name="itens['+line+'].valorunt" id="vlrund'+line+'" size="3" type="text" value="" class="form-control form-control-sm"></td>'+
				      '<td><input name="itens['+line+'].quantidade" size="5" id="qtd'+line+'" type="text" value="1" class="form-control form-control-sm"></td>'+
				      '<td><input name="itens['+line+'].valor" size="5" id="vlr'+line+'" type="text" value="" class="form-control form-control-sm"></td>'+
				      '<td><button class="btn btn-sm btn-danger" onclick="removeRow(this)" type="button">Remover</button></td>'+
				    '</tr>';
          $("table tbody").append(markup);
          CarregaProduto(line);
     });
	 
	 $( "#btnVenda" ).click(function() {
	    	bootbox.confirm("Confirma esse(s) Item(ns)?", function(result){ 		  
	           if (result){
	        	   $("#frmVenda" ).submit();
	    	   } 	
	    	});
	 });

    CarregaCliente();
    CarregaProduto(0);   		
 });
 
 function getValor(idObj,id){
	var s = document.getElementById("produto"+idObj);
	//var qtd = document.getElementById("qtd"+idObj).value;
	//var texto = s.options[s.selectedIndex].text;
	var valor = s.value;
	 $.getJSON( "../produtos/getBeanJson?id="+id, function( data  ) {
 	   }).done (function(data){
 		     totais(data,idObj);
 	   });
 }


 function totais(data,idObj){
	var qtd = document.getElementById("qtd"+idObj).value;
	var subtotal = (data.valor * qtd);
	var total = (document.getElementById("tot").value.replace(',','.') * 100)/100;
	document.getElementById("vlr"+idObj).value = subtotal.toFixed(2).replace('.',',');
	document.getElementById("vlrund"+idObj).value = data.valor.toFixed(2).replace('.',',');
	total = (((total + (subtotal))* 100)/100).toFixed(2);
	document.getElementById("tot").value = total.replace('.',',');
  }

  removeRow = function(handler) {
	    var tr = $(handler).closest('tr');
	    tr.fadeOut(400, function(){ 
	      tr.remove(); 
	    }); 
	    return false;
	  };
	  
</script>	


<jsp:include page = "/footer.inc.jsp" />