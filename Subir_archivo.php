{source}
<?php
include_once 'images/config.inc.php';
if (isset($_POST['subir'])) {
$empresa = 1;
$estatus = 1;
$nombre = $_FILES['archivo']['name'];
$ruta = $_FILES['archivo']['tmp_name'];
$destino = "archivos/" . $nombre;
if ($nombre != "") {
if (copy($ruta, $destino)) {
$db=new Conect_MySql();

$selectOption=$_POST['Tablas'];
$sql = "INSERT INTO bdsgc.$selectOption(empresa,titulo,url,estatus) VALUES('1','$nombre','$destino','1')";
$query = $db->execute($sql);
if($query){
echo "            ¡Se subió correctamente el archivo!";
}
} else {
echo "Error";
}
}
}
?>
<body>
<div style="width: 500px;margin: auto;border: 1px solid blue;padding: 30px;">
<h4>Subir Archivo </h4>
<form method="post" action="" enctype="multipart/form-data">
<select name="Tablas">
<option value ="formatoregistrosbk" >Formatos de registro</option>
<option selected value ="procedimientosbk">Procedimientos</option>
</select>
<table>
<tr>
<td colspan="2"><input type="file" name="archivo"></td>
<tr>
<td><input type="submit" value="Subir" name="subir"></td>
</tr>
</table>
</form> 
</div>
</body>

{/source}