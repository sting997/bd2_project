
CREATE TABLE   `typy_klientow` (
  `id_typu_klienta` TINYINT UNSIGNED NOT NULL,
  `typ` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id_typu_klienta`),
  UNIQUE INDEX `typ_UNIQUE` (`typ` ASC));

CREATE TABLE   `adresy` (
  `id_adresu` INT UNSIGNED NOT NULL,
  `miasto` VARCHAR(45) NOT NULL,
  `ulica` VARCHAR(45) NOT NULL,
  `nr_domu` SMALLINT UNSIGNED NOT NULL,
  `kod_pocztowy` VARCHAR(15) NOT NULL,
  `nr_mieszkania` INT UNSIGNED NULL,
  PRIMARY KEY (`id_adresu`));

CREATE TABLE   `klienci` (
  `id_klienta` INT UNSIGNED NOT NULL,
  `imie` VARBINARY(90) NOT NULL,
  `nazwisko` VARBINARY(90) NOT NULL,
  `adres_id_adresu` INT UNSIGNED NOT NULL,
  `telefon` VARBINARY(24) NOT NULL,
  `email` VARBINARY(128) NOT NULL,
  `numer_konta` VARBINARY(52) NOT NULL,
  `typy_klientow_id_typu_klienta` TINYINT UNSIGNED NOT NULL,
  `rabat` FLOAT UNSIGNED NOT NULL,
  `photo` BLOB NULL,
  PRIMARY KEY (`id_klienta`),
  UNIQUE INDEX `telefon_UNIQUE` (`telefon` ASC),
  INDEX `typy_klientow_id_typu_klienta_idx` (`typy_klientow_id_typu_klienta` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `numer_konta_UNIQUE` (`numer_konta` ASC),
  INDEX `adres_id_adresu_idx` (`adres_id_adresu` ASC),
  CONSTRAINT `fk_klienci_typy_klientow`
    FOREIGN KEY (`typy_klientow_id_typu_klienta`)
    REFERENCES  `typy_klientow` (`id_typu_klienta`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_klienci_adresy`
    FOREIGN KEY (`adres_id_adresu`)
    REFERENCES  `adresy` (`id_adresu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `karnety` (
  `id_karnetu` INT UNSIGNED NOT NULL,
  `klienci_id_klienta` INT UNSIGNED NOT NULL,
  `data_waznosci` DATETIME NOT NULL,
  `cena_karnetu` DECIMAL UNSIGNED NOT NULL,
  PRIMARY KEY (`id_karnetu`),
  INDEX `klienci_id_klienta_idx` (`klienci_id_klienta` ASC),
  CONSTRAINT `fk_karnety_klienci`
    FOREIGN KEY (`klienci_id_klienta`)
    REFERENCES  `klienci` (`id_klienta`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `stadiony` (
  `id_stadionu` TINYINT UNSIGNED NOT NULL,
  `nazwa_stadionu` VARCHAR(45) NOT NULL,
  `adresy_id_adresu` INT UNSIGNED NOT NULL,
  `czynny_od` TIME NOT NULL,
  `czynny_do` TIME NOT NULL,
  PRIMARY KEY (`id_stadionu`),
  INDEX `adresy_id_adresu_idx` (`adresy_id_adresu` ASC),
  CONSTRAINT `fk_stadiony_adresy`
    FOREIGN KEY (`adresy_id_adresu`)
    REFERENCES  `adresy` (`id_adresu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `typy_sektora` (
  `id_typu` TINYINT UNSIGNED NOT NULL,
  `nazwa_typu` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_typu`),
  UNIQUE INDEX `nazwa_typu_UNIQUE` (`nazwa_typu` ASC));

CREATE TABLE   `sektory` (
  `id_sektora` TINYINT UNSIGNED NOT NULL,
  `stadiony_id_stadionu` TINYINT UNSIGNED NOT NULL,
  `typy_sektora_id_typu` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_sektora`),
  INDEX `stadiony_id_stadionu_idx` (`stadiony_id_stadionu` ASC),
  INDEX `typy_sektora_id_typu_idx` (`typy_sektora_id_typu` ASC),
  CONSTRAINT `fk_sektory_stadiony`
    FOREIGN KEY (`stadiony_id_stadionu`)
    REFERENCES  `stadiony` (`id_stadionu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_sektory_typy_sektora`
    FOREIGN KEY (`typy_sektora_id_typu`)
    REFERENCES  `typy_sektora` (`id_typu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `miejsca` (
  `id_miejsca` INT UNSIGNED NOT NULL,
  `sektory_id_sektora` TINYINT UNSIGNED NOT NULL,
  `nr_rzedu` INT UNSIGNED NOT NULL,
  `nr_miejsca` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_miejsca`),
  INDEX `sektory_id_sektora_idx` (`sektory_id_sektora` ASC),
  UNIQUE INDEX `index3` (`sektory_id_sektora` ASC, `nr_rzedu` ASC, `nr_miejsca` ASC),
  CONSTRAINT `fk_miejsca_sektory`
    FOREIGN KEY (`sektory_id_sektora`)
    REFERENCES  `sektory` (`id_sektora`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `typy_wydarzen` (
  `id_typu` TINYINT UNSIGNED NOT NULL,
  `nazwa_typu` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_typu`),
  UNIQUE INDEX `nazwa_typu_UNIQUE` (`nazwa_typu` ASC));

CREATE TABLE   `wydarzenia` (
  `id_wydarzenia` INT UNSIGNED NOT NULL,
  `nazwa` VARCHAR(45) NOT NULL,
  `data_wydarzenia` DATETIME NOT NULL,
  `stadiony_id_stadionu` TINYINT UNSIGNED NOT NULL,
  `typy_wydarzen_id_typu` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_wydarzenia`),
  INDEX `typy_wydarzen_id_typu_idx` (`typy_wydarzen_id_typu` ASC),
  INDEX `stadiony_id_stadionu_idx` (`stadiony_id_stadionu` ASC),
  CONSTRAINT `fk_wydarzenia_typy_wydarzen`
    FOREIGN KEY (`typy_wydarzen_id_typu`)
    REFERENCES  `typy_wydarzen` (`id_typu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_wydarzenia_stadiony`
    FOREIGN KEY (`stadiony_id_stadionu`)
    REFERENCES  `stadiony` (`id_stadionu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `statusy_biletow` (
  `id_statusu` TINYINT UNSIGNED NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_statusu`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC));

CREATE TABLE   `bilety` (
  `id_biletu` INT UNSIGNED NOT NULL,
  `klienci_id_klienta` INT UNSIGNED NOT NULL,
  `miejsca_id_miejsca` INT UNSIGNED NOT NULL,
  `wydarzenie_id_wydarzenia` INT UNSIGNED NOT NULL,
  `data_sprzedazy` DATETIME NOT NULL,
  `statusy_biletow_id_statusu` TINYINT UNSIGNED NOT NULL,
  `karnety_id_karnetu` INT UNSIGNED NULL,
  `cena_biletu` DECIMAL UNSIGNED NOT NULL,
  `uzyskany_upust` DECIMAL UNSIGNED NOT NULL,
  PRIMARY KEY (`id_biletu`),
  INDEX `klienci_id_klienta_idx` (`klienci_id_klienta` ASC),
  INDEX `miejsca_id_miejsca_idx` (`miejsca_id_miejsca` ASC),
  INDEX `karnety_id_karnetu_idx` (`karnety_id_karnetu` ASC),
  INDEX `statusy_biletow_id_statusu_idx` (`statusy_biletow_id_statusu` ASC),
  INDEX `wydarzenie_id_wydarzenia_idx` (`wydarzenie_id_wydarzenia` ASC),
  CONSTRAINT `fk_bilety_klienci`
    FOREIGN KEY (`klienci_id_klienta`)
    REFERENCES  `klienci` (`id_klienta`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_bilety_miejsca`
    FOREIGN KEY (`miejsca_id_miejsca`)
    REFERENCES  `miejsca` (`id_miejsca`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_bilety_wydarzenia`
    FOREIGN KEY (`wydarzenie_id_wydarzenia`)
    REFERENCES  `wydarzenia` (`id_wydarzenia`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_bilety_karnety`
    FOREIGN KEY (`karnety_id_karnetu`)
    REFERENCES  `karnety` (`id_karnetu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_bilety_statusy_biletow`
    FOREIGN KEY (`statusy_biletow_id_statusu`)
    REFERENCES  `statusy_biletow` (`id_statusu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `ceny` (
  `id_ceny` INT NOT NULL,
  `wydarzenie_id_wydarzenia` INT UNSIGNED NOT NULL,
  `sektory_id_sektora` TINYINT UNSIGNED NOT NULL,
  `cena` DECIMAL UNSIGNED NOT NULL,
  INDEX `sektory_id_sektora_idx` (`sektory_id_sektora` ASC),
  INDEX `wydarzenie_id_wydarzenia_idx` (`wydarzenie_id_wydarzenia` ASC),
  PRIMARY KEY (`id_ceny`),
  CONSTRAINT `fk_ceny_sektory`
    FOREIGN KEY (`sektory_id_sektora`)
    REFERENCES  `sektory` (`id_sektora`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ceny_wydarzenia`
    FOREIGN KEY (`wydarzenie_id_wydarzenia`)
    REFERENCES  `wydarzenia` (`id_wydarzenia`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE   `rezerwacje` (
  `id_rezerwacji` INT UNSIGNED NOT NULL,
  `klienci_id_klienta` INT UNSIGNED NOT NULL,
  `bilety_id_biletu` INT UNSIGNED NOT NULL,
  `data_zlozenia` DATETIME NOT NULL,
  `termin_wygasniecia` DATETIME NOT NULL,
  `zrealizowana` BIT NOT NULL,
  PRIMARY KEY (`id_rezerwacji`),
  INDEX `klienci_id_klienta_idx` (`klienci_id_klienta` ASC),
  INDEX `bilety_id_biletu_idx` (`bilety_id_biletu` ASC),
  CONSTRAINT `fk_rezerwacje_klienci`
    FOREIGN KEY (`klienci_id_klienta`)
    REFERENCES  `klienci` (`id_klienta`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rezerwacje_bilety`
    FOREIGN KEY (`bilety_id_biletu`)
    REFERENCES  `bilety` (`id_biletu`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
