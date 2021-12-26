<?php

include('koneksi.php');

$nama 		= $_POST['nama'];
$nim		= $_POST['nim'];
$prodi		= $_POST['prodi'];
$nohp 		= $_POST['nohp'];

if(!empty($nama) && !empty($nim)){

	$sql = "UPDATE mahasiswa set nama='$nama', prodi='$prodi', nohp='$nohp' WHERE nim='$nim' ";

	$query = mysqli_query($conn,$sql);

	if(mysqli_affected_rows($conn) > 0){
		$data['status'] = true;
		$data['result']	= "Berhasil";
	}else{
		$data['status'] = false;
		$data['result']	= "Gagal";
	}

}else{
	$data['status'] = false;
	$data['result']	= "Gagal, NIM dan Nama tidak boleh kosong!";
}


print_r(json_encode($data));




?>