ALTER TABLE cash_registers
ADD COLUMN expected_cash DECIMAL(10,2),
ADD COLUMN expected_pix DECIMAL(10,2),
ADD COLUMN expected_credit DECIMAL(10,2),
ADD COLUMN expected_debit DECIMAL(10,2),
ADD COLUMN counted_cash DECIMAL(10,2),
ADD COLUMN counted_pix DECIMAL(10,2),
ADD COLUMN counted_credit DECIMAL(10,2),
ADD COLUMN counted_debit DECIMAL(10,2),
ADD COLUMN total_difference DECIMAL(10,2);
