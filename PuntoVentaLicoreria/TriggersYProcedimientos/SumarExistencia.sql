delimiter //
DROP TRIGGER IF EXISTS SumarExistencia //
CREATE TRIGGER  SumarExistencia
AFTER INSERT ON detallecompra
FOR EACH ROW
BEGIN
    UPDATE inventario SET Existencia = (Existencia + NEW.Cantidad) WHERE (id = NEW.Inventario_ID);
end ;//
delimiter ;