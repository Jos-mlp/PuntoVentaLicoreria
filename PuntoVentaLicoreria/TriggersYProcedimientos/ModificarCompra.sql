delimiter //
DROP TRIGGER IF EXISTS ModificarCompra //
CREATE TRIGGER ModificarCompra
BEFORE INSERT ON compra
FOR EACH ROW
BEGIN
	SET NEW.Fecha = NOW();
end ;//
delimiter ;