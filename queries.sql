select count(*) from bilety where wydarzenie_id_wydarzenia = 1 group by statusy_biletow_id_statusu;
select count(*) from klienci group by typy_klientow_id_typu_klienta;
select sum(cena_biletu) from bilety inner join wydarzenia where data_wydarzenia between '2000-07-05' AND '2021-11-10';
select sum(cena_biletu), typy_klientow_id_typu_klienta from bilety inner join klienci on klienci_id_klienta = id_klienta where data_sprzedazy BETWEEN '2000-07-05' AND '2021-11-10' group by typy_klientow_id_typu_klienta;
select sum(uzyskany_upust) as suma, typy_klientow_id_typu_klienta as typ_klienta, wydarzenie_id_wydarzenia as wydarzenie from bilety inner join klienci on klienci_id_klienta = id_klienta where data_sprzedazy BETWEEN '2000-07-05' AND '2021-11-10' group by typy_klientow_id_typu_klienta, wydarzenie_id_wydarzenia;

