<?php

include('koneksi.php');

$nama 		= $_POST['nama'];
$nim	= $_POST['nim'];
$prodi		= $_POST['prodi'];
$nohp 		= $_POST['nohp'];

if(!empty($nama) && !empty($nim)){

	$sqlCheck = "SELECT COUNT(*) FROM mahasiswa WHERE nim='$nim' AND nama='$nama'";
	$queryCheck = mysqli_query($conn,$sqlCheck);
	$hasilCheck = mysqli_fetch_array($queryCheck);
	if($hasilCheck[0] == 0){
		$sql = "INSERT INTO mahasiswa (nama,nim,prodi,nohp) VALUES('$nama','$nim','$prodi','$nohp')";

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
		$data['result']	= "Gagal, Data Sudah Ada";
	}

	

}
else{
	$data['status'] = false;
	$data['result']	= "Gagal, NIM dan Nama tidak boleh kosong!";
}


print_r(json_encode($data));




?>