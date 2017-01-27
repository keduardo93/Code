{source}
<?php
require('images/conexion.php');
include 'images/config.inc.php';

$sql = "SELECT nombre,codigo,version,tipo,archivo FROM bdsgc.formatoregistrosbk";
$result=$mysqli->query($sql);
?>
<html>
<html lang = "es">
<head>
<meta charset ="utf-8"/>
<meta description = "Sistema de Gestion de Calidad ITCM"/>
<title>Sistema de Gestión de Calidad</title> 


<script type="text/javascript" language="javascript" src="images/js/jquery.js">
</script>
<script type="text/javascript" language="javascript" src="images/js/jquery.dataTables.min.js">
</script>

<link rel="stylesheet" type="text/css" href="images/css/jquery.dataTables.min.css">

<script>
$(document).ready(function() {
$('#example').DataTable({
"order": [[ 1, "asc" ]],
"language": {
"lengthMenu": " Mostrar _MENU_ registros por página",
"info": "Mostrando página _PAGE_ de _PAGES_",
"infoEmpty": "No records available",
"infoFiltered": "(filtrada de _MAX_ registros)",
"search": "Buscar:",
"paginate": {
"next": "Siguiente",
"previous": "Anterior"
},
}
});
} );

</script>
</head>

<body >


<main> 

<section> 
<table id= "example" width="95%" > 
<thead>
<tr>
<th>Nombre</th>
<th>Codigo</th>
<th>Versión</th>
<th>Tipo</th>
<th>Descarga</th>
</tr>

</thead>
<tbody >
<?php while($row = $result->fetch_assoc()){ ?>
<tr>
<td  ><?php echo $row['nombre']; ?></td>
<td  ><?php echo $row['codigo']; ?></td>
<td  ><?php echo $row['version']; ?></td>
<td  ><?php echo $row['tipo']; ?></td>
<td><a href="<?php echo $row['archivo']; ?>">Enlace</a></td>
</tr>
<?php } ?>
</tbody>
</table>

</section> 
</main> 
</body> 

</html>
{/source} 