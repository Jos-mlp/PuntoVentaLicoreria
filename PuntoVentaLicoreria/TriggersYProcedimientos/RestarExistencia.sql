delimiter //
DROP TRIGGER IF EXISTS RestarExistencia //
CREATE TRIGGER  RestarExistencia
AFTER INSERT ON detalleventa
FOR EACH ROW
BEGIN
    UPDATE inventario SET Existencia = (Existencia - NEW.Cantidad) WHERE (id = NEW.Inventario_ID);
end ;//
delimiter ;