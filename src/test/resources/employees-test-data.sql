BEGIN;
SET session_replication_role = 'replica';
truncate table public.assignment cascade;
truncate table public.project cascade;
truncate table public.client cascade;
truncate table public.company cascade;
truncate table public.config cascade;
truncate table public.contract cascade;
truncate table public.country cascade;
truncate table public.employee cascade;
truncate table public.employee_role cascade;
truncate table public.employee_technology cascade;
truncate table public.holiday_calendar cascade;
truncate table public.leave_type cascade;
truncate table public.payment_information cascade;
truncate table public.payment_settlement cascade;
truncate table public.project_type cascade;
truncate table public.pto cascade;
truncate table public.role cascade;
truncate table public.seniority cascade;
truncate table public.technology cascade;
truncate table public.tenant cascade;
truncate table public.vacation cascade;
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('dea74f8e-558b-4a0f-a3d8-b0d84c5c783f', '2023-02-03 00:00:00.000000', '2023-08-23 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 12, 32221, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'fb7c10dd-1db0-4d5d-af2b-799d874e7729', '2023-02-23 12:38:13.185926', '2023-08-23 20:22:29.573166', true, 'USD', '32', false, 'this is an end reason jdsjadjsaiñdjiafjiñaofjelñafhewulahfewalhfdewÑJEAFJEWOOFHJEUFHJEW{JFew{jfewñihfeuiwlfewfeñwJFEWFewafdykuewfkefekwgfyeklwGDFYKEWGH');
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('54ccbe80-2cb5-462e-96a1-d5bc95cb048f', '2023-07-07 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 12, 52, 'd4748178-362b-413d-be80-ea4f4785e984', 'ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', '2023-07-07 16:21:21.299597', '2023-08-23 20:22:29.584208', true, 'USD', '1', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('fbb139f0-77bf-431d-b840-2775d31bc3dd', '2023-01-20 00:00:00.000000', '2023-08-23 00:00:00.000000', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 40, 10, '38e96879-a7cc-44a4-94b6-2225d1c853f2', '5dea6ed4-63aa-434e-b50d-610b08df4843', '2023-01-19 20:33:29.243055', '2023-08-23 20:22:29.701612', true, 'USD', '4-9', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('313db7cb-21e5-41c2-8d96-778ba50767af', '2023-01-19 00:00:00.000000', null, '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 77, 77, '38e96879-a7cc-44a4-94b6-2225d1c853f2', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-01-19 20:36:55.897711', '2023-01-19 20:36:55.897722', false, 'USD', '77', true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('17fa31c8-c535-4c73-81de-069d63b75bde', '2023-03-01 00:00:00.000000', '2023-08-23 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', null, null, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'cfcbb961-947a-4dee-a7f0-3582de2cb6c7', '2023-03-15 14:54:17.564176', '2023-08-23 20:22:29.704312', true, null, null, false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('addfe084-de91-4082-b0e5-c6446ec4e6f2', '2022-12-01 00:00:00.000000', '2023-01-31 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 40, 123, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2022-12-30 13:39:15.234603', '2023-01-02 19:48:27.150921', true, 'USD', '40', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('9be61b32-6176-43d3-824e-71870b086320', '2023-09-09 00:00:00.000000', '2023-10-10 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 20, 1500, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-02-03 12:52:11.240397', '2023-12-14 14:20:17.390957', false, 'USD', '40', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('157884ad-2347-45ab-abb4-75d4d60eee0d', '2023-02-01 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 44, 20, '38e96879-a7cc-44a4-94b6-2225d1c853f2', '5dea6ed4-63aa-434e-b50d-610b08df4843', '2023-01-19 20:36:19.708402', '2023-08-23 20:22:29.705524', true, 'USD', '6-8', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('d81d5699-9c53-4d5a-a9bf-8a80441ee1c5', '2023-12-11 00:00:00.000000', '2023-12-13 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', null, null, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '2023-12-14 13:52:07.946279', '2023-12-14 14:20:17.393053', false, null, null, false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('c4f99bd0-b202-4669-a219-f74addd2f469', '2023-09-18 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'b79b259d-92cf-4336-abae-007d92ac6a08', 158, 15, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'd102b786-7f8a-42b4-ab49-30b8390ee173', '2023-09-15 12:35:17.308330', '2023-12-14 14:20:17.408270', false, 'USD', '9-18', true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('e39ceddb-88f7-46b4-96e2-e5fc26a28d9a', '2023-01-19 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', null, 10, '38e96879-a7cc-44a4-94b6-2225d1c853f2', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-01-19 21:01:07.766079', '2023-01-19 21:01:07.766091', false, 'USD', null, false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('a4673bf0-b1ea-49bb-9f20-fe5be4a0edc1', '2023-02-01 00:00:00.000000', null, '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 44, 4, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-01-20 13:07:28.631604', '2023-01-20 13:07:28.631625', false, 'USD', '4', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('9839817c-e2e8-4da9-b87a-a543068ad57d', '2023-02-23 00:00:00.000000', null, '6d85225e-5d41-447e-aaac-7f11ce593c97', 'b79b259d-92cf-4336-abae-007d92ac6a08', 32, 12, '8aa951ed-8020-4dee-b03b-7ab458b55a00', '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-02-10 20:50:57.403486', '2023-02-10 20:50:57.403495', false, 'ARS', '13', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('3c0dd542-09f0-4475-a7c1-060725cfa280', '2023-12-14 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', null, null, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '2023-12-14 13:54:30.798269', '2023-12-14 14:20:17.411327', false, null, null, true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('6c103cf1-d9df-4633-9711-c47c953fc378', '2022-06-06 00:00:00.000000', '2023-07-07 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 35, 2354, '38e96879-a7cc-44a4-94b6-2225d1c853f2', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-02-03 12:58:37.746485', '2023-02-23 12:35:09.495891', false, 'ARS', '37', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('01fb7e63-44ef-45d3-b927-c83fd5c61226', '2023-03-01 00:00:00.000000', '2023-08-23 00:00:00.000000', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 'b79b259d-92cf-4336-abae-007d92ac6a08', null, null, '8aa951ed-8020-4dee-b03b-7ab458b55a00', 'cfcbb961-947a-4dee-a7f0-3582de2cb6c7', '2023-03-15 15:27:45.874159', '2023-08-23 20:22:29.708162', true, null, null, false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('7e795bc6-5c84-45bc-898a-ca4e6103ba2a', '2023-08-24 00:00:00.000000', null, '13b9db74-e63f-4c50-a338-49a930cf52c4', 'a0d3a765-92af-4af1-94c5-d496d84a449e', 159, 5, '8aa951ed-8020-4dee-b03b-7ab458b55a00', '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-08-24 12:47:51.846088', '2023-08-24 12:47:51.846108', false, 'USD', '9-18', true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('3f096eb1-8db2-43b8-b909-69757f322502', '2023-12-26 00:00:00.000000', null, 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', '6e65f32c-adde-4dc5-89e9-fd65dd2d5a0b', 161, 25, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '09f38a52-e6fe-4f24-b39a-d1ec507eca61', '2023-12-21 19:52:42.516040', '2024-01-06 09:05:00.095047', false, 'USD', '9-18', true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('839f03d4-2e13-45c0-9b1b-96c4f0c4e5e0', '2023-02-08 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 434, 11, '8aa951ed-8020-4dee-b03b-7ab458b55a00', '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-02-10 20:50:24.121245', '2024-04-12 13:25:32.389743', false, 'USD', '9-18', true, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('316b1384-0cc1-4d25-8448-7a8aca8cf54e', '2023-08-17 00:00:00.000000', '2023-08-18 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', null, null, 'd4748178-362b-413d-be80-ea4f4785e984', '36f7c26a-7142-43e9-8a6a-fb936c9ace88', '2023-08-18 15:50:20.367213', '2023-08-18 15:51:47.322177', true, null, null, false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('affd2997-11a7-4258-8705-fbfd7be2c25e', '2023-07-07 00:00:00.000000', '2023-08-23 00:00:00.000000', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 'b79b259d-92cf-4336-abae-007d92ac6a08', 12, 12, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '7ffec6f5-d8c5-47ab-90b5-959a0c294295', '2023-07-07 16:34:13.698986', '2023-08-23 20:22:29.543309', true, 'USD', '12', false, 'le hace mal trabajar');
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('529ff14b-fb27-4f65-8bba-86b2dee33cb0', '2023-04-17 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'b79b259d-92cf-4336-abae-007d92ac6a08', 12, 122, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', 'ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', '2023-04-17 14:33:32.030677', '2023-08-23 20:22:29.568700', true, 'USD', '1', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('3e1d214b-38b1-4f53-8088-cb5565633885', '2023-01-31 00:00:00.000000', '2023-08-31 00:00:00.000000', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 'b79b259d-92cf-4336-abae-007d92ac6a08', 20, 46, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-01-19 19:59:40.608154', '2023-08-31 12:47:58.881841', false, 'USD', '44', false, null);
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('1e052cb1-24c7-47da-8e84-661ac09ce0fe', '2023-01-31 00:00:00.000000', '2023-09-08 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 20, 46, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-08-31 12:47:50.527073', '2023-09-08 13:19:01.885897', false, 'USD', '44', false, '99');
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('64a3dd1a-dd8f-4bf6-a45d-8b5314c52c2d', '2022-12-29 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 150, 20, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2022-12-29 18:40:59.330016', '2023-09-08 13:19:01.898252', false, 'USD', '9-20', true, '99');
INSERT INTO public.assignment (id, start_date, end_date, role_id, seniority_id, hours_per_month, billable_rate, project_id, employee_id, created_at, modified_at, deleted, currency, labour_hours, active, end_reason) VALUES ('96447d85-f390-47ff-a0f8-65db5bdef04e', '2022-12-01 00:00:00.000000', '2022-12-31 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 40, 1500, 'ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '638bd1e1-5e18-42a5-b2d1-ba1086713634', '2022-12-30 13:52:12.574760', '2023-12-14 14:20:17.381312', false, 'USD', '40', false, null);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('e8afb2f8-a146-4ab4-81a0-2d090f644042', 'A new client', null, null, null, null, 'Argentina', null, 'ARS', '2023-02-23 12:39:55.623312', '2023-02-23 12:39:55.623325', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', null, null, true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('a4e8997d-b7dc-454a-9a32-616c2ceac687', 'oliver', 'sdvdr', 'grer', 'gr', 'rgre', 'rgr', 'rg', 'USD', '2024-01-10 15:33:53.636792', '2024-01-10 15:34:18.872748', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', 'rgeg@gmail.com', null, true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('fd347811-4f77-4639-a55b-515beb213a86', 'Basic client no address test', 'egfr', 'rgrg', 'grg', 'rg', 'grr', 'rgr', 'rgr', '2023-02-27 15:35:56.084146', '2024-01-10 15:38:28.665609', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', 'rgr', null, true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('c3581ab5-dd05-49a1-a93e-2c94edd7b425', 'NewClient', null, null, null, null, null, null, null, '2024-01-22 13:15:20.185728', '2024-01-22 13:15:20.185749', false, '0eca508e-e3d0-486a-b708-a1b192842670', null, null, true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('4acb3450-e1af-40d6-8efe-20ae6fecfa5f', 'Vehlo', 'fgbhfsh', '541646', 'Dallas', 'texas', 'USA', 'Pepito Flores', 'USD', '2022-12-26 16:13:05.744455', '2024-01-05 13:17:27.253796', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', 'contactemail@test', 'c007', true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('67f70cc7-497c-4ad4-9108-1f1fc7664070', 'Steam', null, null, null, null, null, null, null, '2024-02-16 12:09:56.295896', '2024-02-16 12:09:56.295914', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', null, 'c0003', true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('1e86fc8c-bd47-41e6-b3f0-25a137490062', 'reger', 'regerg', '5465', 'Dallas', 'Texas', 'USA', 'akjndkj', 'usd', '2023-01-16 17:23:01.129917', '2024-02-16 12:10:12.194109', false, '6ae4758c-04f9-455d-9e54-eabc386d02c2', 'dscsadv@kflk.com', 'c33', true);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('084d591b-9260-4c67-a824-9121db5c3996', 'Awesome client', '25 de mayo', '789798', 'Tandil', 'Buenos Aires', 'Argentina', 'a contact', 'ARS', '2023-02-10 20:45:23.805993', '2024-01-10 15:39:42.000522', true, '0eca508e-e3d0-486a-b708-a1b192842670', 'dsadasd@dsadsa.com', null, false);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('2fb7730f-fab7-465a-a612-c40d1a59473c', 'InactiveClient', null, null, null, null, null, null, null, '2024-02-22 13:44:41.335195', '2024-02-22 13:44:41.335204', false, '0eca508e-e3d0-486a-b708-a1b192842670', null, null, false);
INSERT INTO public.client (id, name, address_line, zip_code, city, state, country, contact_full_name, preferred_currency, created_at, modified_at, deleted, company_id, contact_email, internal_id, active) VALUES ('6aee54c6-044c-4a4f-9575-65da2abee387', 'NewClient', null, null, null, null, null, null, null, '2024-01-19 13:38:06.880130', '2024-02-22 13:52:38.562782', false, '0eca508e-e3d0-486a-b708-a1b192842670', null, null, true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('6ae4758c-04f9-455d-9e54-eabc386d02c2', '251141ab-298f-437b-a6ab-4984cafaefe0', 'EntropyLLC', '2035 Sunset Lake Road, Suite b-2', '19702', 'Newark', 'Delaware', 'United States', '2022-11-08 10:45:24.904104', '2022-11-08 10:45:24.904104', false);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('d0078144-fdb2-4f27-84c3-bd232eed1ea0', '251141ab-298f-437b-a6ab-4984cafaefe0', 'test company agustin', '25 de mayo', '7000', 'Tandil', 'Buenos Aires', 'ARge', '2023-01-06 13:37:51.102913', '2023-01-26 15:19:57.613219', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('0eca508e-e3d0-486a-b708-a1b192842670', '251141ab-298f-437b-a6ab-4984cafaefe0', 'My comany', 'calle falsa 123', '123', 'Tandil', 'Buenos Aires', 'Argentina', '2023-02-10 20:44:04.437169', '2023-02-10 20:44:24.429157', false);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('eec6bd14-3b22-49a2-ab96-a170419e3482', '251141ab-298f-437b-a6ab-4984cafaefe0', 'Basic company no address test', null, null, null, null, 'Argentina', '2023-02-23 13:28:04.627486', '2023-02-27 15:34:22.142913', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('b2d19bef-3b74-4309-bc0e-79d09dbd646b', '251141ab-298f-437b-a6ab-4984cafaefe0', 'Basic company no address test', null, null, null, null, 'Argentina', '2023-02-23 13:27:26.013156', '2023-02-27 15:34:22.173381', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('32a1da04-4f13-4551-aabc-5259acab86eb', '251141ab-298f-437b-a6ab-4984cafaefe0', 'nueva', null, null, null, null, null, '2023-02-24 13:41:08.012553', '2023-08-24 12:58:18.593793', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('78071036-f5be-471d-8384-339119019127', '251141ab-298f-437b-a6ab-4984cafaefe0', 'My other company', null, null, null, null, 'Argentina', '2023-02-23 12:44:35.295589', '2023-08-24 12:58:18.594468', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('5981b652-feae-4774-8ca3-0b29c49ac663', '251141ab-298f-437b-a6ab-4984cafaefe0', 'New company no address line test 2', null, null, null, null, 'Argentina', '2023-02-23 13:30:54.258163', '2023-08-24 12:58:18.595824', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('41960937-16df-4dae-9b69-08e47e7ad2b1', '251141ab-298f-437b-a6ab-4984cafaefe0', 'gghj', null, null, null, null, 'Argentina', '2023-02-15 18:52:09.054267', '2023-08-24 12:58:18.596484', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('87008490-c6b6-4286-bb49-7f8c72b47b30', '251141ab-298f-437b-a6ab-4984cafaefe0', 'Company edit Test', 'fake street 123', '11111', 'BS AS', 'BS AS', 'Argentina', '2022-11-24 15:21:30.128418', '2023-08-24 12:58:18.597162', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('73a0ae96-109b-4ee6-bd0b-f018a9ecbc44', '251141ab-298f-437b-a6ab-4984cafaefe0', 'My comany', null, null, 'Tandil', null, null, '2023-02-15 16:14:41.720970', '2023-08-24 12:58:18.593176', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('78318f0b-e282-4dc0-a89d-8fd529fbd1be', '251141ab-298f-437b-a6ab-4984cafaefe0', 'test company fail', null, null, 'Tandil', null, null, '2023-01-06 13:46:23.423025', '2023-08-24 12:58:18.597610', true);
INSERT INTO public.company (id, tenant_id, name, address_line, zip_code, city, state, country, created_at, modified_at, deleted) VALUES ('81777b98-6e73-4fc5-b09c-a3bae807a6c4', '251141ab-298f-437b-a6ab-4984cafaefe0', 'Basic company no address last test', null, null, null, null, null, '2023-02-27 15:34:54.034128', '2023-08-24 12:58:18.609965', true);
INSERT INTO public.config (id, role, permissions, menu, created_at, modified_at, deleted) VALUES ('3c245af9-19fc-4e02-b9b0-a85e8ed06594', 'ROLE_DEVELOPMENT', e'[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]}
]', e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 2
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 3
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 4
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 62
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 63
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 64
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 65
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 66
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 67
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 68
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 69
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 611
      }
    ]
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 7,
    "submenu": [
      {
        "name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      }
    ]
  }
]', '2022-12-28 15:19:29.000000', '2022-12-28 15:19:29.000000', false);
INSERT INTO public.config (id, role, permissions, menu, created_at, modified_at, deleted) VALUES ('8fe02eb2-2d93-40c5-8ebe-1bbfa86c9456', 'ROLE_ANALYST', e'[
  {"entity": "employees", "actions": ["read"]},
  {"entity": "contracts", "actions": ["read"]},
  {"entity": "assignments", "actions": ["read"]},
  {"entity": "clients", "actions": ["read"]}
]', e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 2
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 3
  }
]', '2022-12-28 15:19:29.000000', '2022-12-28 15:19:29.000000', false);
INSERT INTO public.config (id, role, permissions, menu, created_at, modified_at, deleted) VALUES ('b1bf3a8c-ced5-443f-90a3-ee9d283a18c0', 'ROLE_MANAGER_HR', e'[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]}
]', e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 2
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 3
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 4
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 62
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 63
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 64
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 65
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 66
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 67
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 68
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 69
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 611
      }
    ]
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 7,
    "submenu": [
      {
        "name": "Employees",
        "href": "/#/reports/employees",
        "key": 71
      },
      {
        "name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      }
    ]
  }
]', '2022-12-28 15:19:29.592000', '2022-12-28 15:19:29.592000', false);
INSERT INTO public.config (id, role, permissions, menu, created_at, modified_at, deleted) VALUES ('160ec32b-b39f-4966-add0-c82e03d2edb7', 'ROLE_HR_DIRECTOR', e'[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]}
]', e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 2
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 3
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 4
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 62
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 63
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 64
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 65
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 66
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 67
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 68
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 69
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 611
      }
    ]
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 7,
    "submenu": [
      {
        "name": "Employees",
        "href": "/#/reports/employees",
        "key": 71
      },
      {
        "name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      }
    ]
  }
]', '2023-08-18 12:42:23.172000', '2023-08-18 12:42:23.172000', false);
INSERT INTO public.config (id, role, permissions, menu, created_at, modified_at, deleted) VALUES ('d172386e-46b3-4c6f-b030-14b9204ea059', 'ROLE_ADMIN', e'[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update", "delete"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]}
]', e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 2
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 3
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 4
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 62
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 63
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 64
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 65
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 66
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 67
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 68
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 69
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 611
      }
    ]
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 7,
    "submenu": [
      {
        "name": "Employees",
        "href": "/#/reports/employees",
        "key": 71
      },
      {
        "name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      }
    ]
  }
]', '2022-12-28 15:19:29.000000', '2023-05-09 11:00:00.000000', false);
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('a097b2fb-9b9b-4f8d-ab5a-3ac48c75f080', '3bad80be-cf5d-42fd-a506-69498fa6674b', '87008490-c6b6-4286-bb49-7f8c72b47b30', '2023-07-18 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 12, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-07-18 12:44:11.334508', '2023-08-23 20:22:29.546103', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('75a20961-e4d3-4da2-831c-68d7e64da461', '001a8e97-069f-471c-bd80-dd168f5cdbfd', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-08-28 00:00:00.000000', '2023-10-06 00:00:00.000000', '458c0c8b-8381-41f7-976e-174dcfbc5751', 160, '6e65f32c-adde-4dc5-89e9-fd65dd2d5a0b', '2023-08-28 16:01:41.924592', '2023-10-06 12:14:17.390797', true, false, null, null, 'MIX');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('a770e5d5-deaa-481e-9b47-ddccd1f6a6a9', '638bd1e1-5e18-42a5-b2d1-ba1086713634', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 100, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 19:04:22.528978', '2023-02-01 14:25:28.223985', false, false, 'benefits', 'aaa', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('e190d3c9-ca3a-4960-b9bb-c1f6fdac674c', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-04 00:00:00.000000', '2023-02-02 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 15, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 18:52:50.799558', '2023-02-02 13:43:35.287441', true, false, null, null, 'MIX');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('f7bae9db-5951-490a-8e80-d190e2980fae', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-03 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 45, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 18:52:01.549269', '2022-12-29 19:29:33.378586', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('7b5656f5-56e3-465e-8a32-e4b2badaf1ef', '638bd1e1-5e18-42a5-b2d1-ba1086713634', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-10 00:00:00.000000', '2022-12-29 00:00:00.000000', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 7, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 19:00:33.051061', '2022-12-29 19:30:22.087191', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('6e981f1d-6f72-45e7-ad60-096c58adfe1f', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2022-12-01 00:00:00.000000', '2022-12-30 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 17:53:46.408978', '2022-12-29 19:30:26.808441', true, false, 'Pedidos Ya', 'Notes test', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('16a4bf20-3424-40fc-ba06-a49774a31a16', 'ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-04-17 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 12, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-04-17 14:33:10.560437', '2023-08-23 20:22:29.553440', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('9cfbae59-e159-4622-bf44-9aeedce86be5', '2c83734b-e42e-4ab0-acce-ba372b0e2744', '0eca508e-e3d0-486a-b708-a1b192842670', '2023-02-13 00:00:00.000000', '2023-02-10 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 12, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-02-10 20:48:51.979475', '2023-02-10 20:49:30.622915', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('fe9fd008-0674-4f93-8990-c6fb2166106a', '638bd1e1-5e18-42a5-b2d1-ba1086713634', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2022-12-07 00:00:00.000000', '2023-01-04 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2022-12-29 19:22:52.546935', '2023-01-04 13:35:29.435698', true, false, null, '32', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('259269ea-aae2-4763-8260-9628cf5d6e82', 'fb7c10dd-1db0-4d5d-af2b-799d874e7729', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-08-09 00:00:00.000000', '2023-08-23 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 12, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-08-09 13:51:48.984445', '2023-08-23 20:22:29.564244', true, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('ca393d63-1d62-4b82-a707-295d9ec287ba', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-09-09 00:00:00.000000', '2023-02-27 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-03 12:50:57.982866', '2023-02-27 15:58:34.924802', false, false, 'Pedidos Ya', 'aaa', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('901c3917-0295-41f9-9ab8-036d8ffc5429', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-02-16 00:00:00.000000', '2023-03-14 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 43, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-22 18:25:08.591795', '2023-03-14 17:41:15.227704', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('a3fb33c2-6957-4e2b-b3f1-48af8a588e80', '7643c2a7-64f2-432f-aaf7-0a3bb16323ad', '87008490-c6b6-4286-bb49-7f8c72b47b30', '2023-07-06 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 123, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-07-18 12:38:27.781334', '2023-08-23 20:22:29.581347', true, false, '12', null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('0d76701d-27e3-4098-a3df-a10d55b0b83b', '61b9ec38-4fdb-4591-9626-0ea32383c814', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2022-01-01 00:00:00.000000', '2023-07-07 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-03 12:54:45.798216', '2023-09-14 14:05:46.343605', true, true, 'Pedidos Ya test', 'aa', 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('6f989f8d-7b15-4619-bc63-978d229c5587', '638bd1e1-5e18-42a5-b2d1-ba1086713634', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', null, '6d85225e-5d41-447e-aaac-7f11ce593c97', 80, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-01-04 13:35:29.481280', '2023-03-31 13:30:56.952586', false, true, 'Benefits test', 'notes', 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('19e5c11d-6397-4a55-846a-4e3b03df0351', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', '2023-02-28 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-27 15:58:34.955677', '2023-04-05 14:59:08.382496', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('2afd618f-a8e7-4c36-9299-98c528711fe0', 'f03bab93-5582-49f7-a04a-e1332f86361f', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2022-05-10 00:00:00.000000', '2023-11-12 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-11-16 12:20:44.920370', '2023-11-16 12:20:44.920382', false, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('044a3ee4-c38b-4d73-bd84-f4804f149338', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', '2023-03-30 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-04-05 15:01:05.523484', '2023-04-05 15:01:05.523500', false, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('91af8248-53b9-40f8-8b04-076a69645efe', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-03-03 00:00:00.000000', '2023-04-13 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-03-14 17:41:15.284676', '2023-04-14 18:44:11.393943', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('dab2cfcc-83ea-44fb-8b97-0c7e63e4be43', 'b5f728c9-cc37-48ab-bfd7-0b341b83f264', '32a1da04-4f13-4551-aabc-5259acab86eb', '2023-08-03 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 123, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-08-03 16:03:48.271480', '2023-08-23 20:22:29.676733', true, false, '132', null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('5919ad55-bce2-471a-8bab-9ba4da5f47ca', '7643c2a7-64f2-432f-aaf7-0a3bb16323ad', '73a0ae96-109b-4ee6-bd0b-f018a9ecbc44', '2023-07-18 00:00:00.000000', '2024-02-27 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 123, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-07-18 12:45:19.669009', '2023-07-18 13:29:10.903760', true, true, '12', null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('834ebbee-b38d-4438-8160-0b7a9633881f', 'bd5951d6-420f-4e41-b055-4f4c31db57ba', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2021-10-13 00:00:00.000000', '2023-04-17 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 20, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-02 13:48:27.395275', '2023-09-14 14:05:46.329276', true, true, 'Benefits test', 'notes', 'TRAINEE');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('948a8bae-9c7e-4f68-aed7-33563eaab4ab', 'cfcbb961-947a-4dee-a7f0-3582de2cb6c7', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-03-01 00:00:00.000000', '2023-08-23 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-03-15 14:53:47.166601', '2023-08-23 20:22:29.681413', true, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('01f03f1a-6191-489d-bc20-8b173902f592', '36f7c26a-7142-43e9-8a6a-fb936c9ace88', '87008490-c6b6-4286-bb49-7f8c72b47b30', '2023-08-15 00:00:00.000000', '2023-08-18 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 70, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-08-18 15:49:13.738841', '2023-08-18 15:51:47.321069', true, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('30ff4d30-e90f-44af-8432-74ac0992e971', 'fb7c10dd-1db0-4d5d-af2b-799d874e7729', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-02-23 00:00:00.000000', '2023-08-23 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', 12, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-23 12:37:43.951845', '2023-08-23 20:22:29.542607', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('737b721e-da6a-489c-80eb-fd4e54e3d380', '5dea6ed4-63aa-434e-b50d-610b08df4843', '87008490-c6b6-4286-bb49-7f8c72b47b30', '2022-01-01 00:00:00.000000', '2023-08-23 00:00:00.000000', '0ce4763c-dbaa-48c5-9018-b16c9f313335', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-01 14:18:32.924145', '2023-08-23 20:22:29.691182', true, false, 'test', 'notes', 'MIX');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('60197408-5e73-4a22-ac6a-d864ba94cc89', 'cfcbb961-947a-4dee-a7f0-3582de2cb6c7', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-03-03 00:00:00.000000', '2023-08-23 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-03-15 15:27:16.735038', '2023-08-23 20:22:29.699195', true, false, null, null, 'EMPLOYEE_RELATIONSHIP');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('adb458bd-d0ec-4ef2-864f-fe50a4b16856', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', '2023-04-10 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-04-05 14:59:45.715212', '2023-08-25 13:14:04.495220', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('9fd1c59a-cf31-4044-83f0-b61281918639', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-03-01 00:00:00.000000', '2024-02-02 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-02-02 13:43:35.317903', '2023-08-31 12:47:22.498104', false, false, 'benefits2', 'notes', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('29cfe7cc-7cc9-4c60-9441-b56662903bb9', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-01-01 00:00:00.000000', '2023-04-05 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-04-05 14:59:08.441162', '2023-10-27 12:40:01.223785', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('54680f2e-5e87-4870-835f-581b8e53dd9d', '24005b6b-718e-4926-b9f5-ac35b5952a7b', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-10-11 00:00:00.000000', '2024-01-13 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'a8b328a1-d6f1-49f0-b712-a3fe7fcc29bf', '2023-11-13 14:39:32.837257', '2024-01-14 09:00:00.366230', false, false, null, null, 'TRAINEE');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('3184c828-9cd9-4130-8afb-20d61a3fae17', '5fa672f5-420e-4737-b2e1-f50fd00120a2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-10-05 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-10-05 12:49:43.543939', '2023-11-02 22:55:35.540176', false, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('64f4d906-fb06-48bf-9dac-9a6abc917a8a', '001a8e97-069f-471c-bd80-dd168f5cdbfd', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-08-24 00:00:00.000000', '2023-08-28 00:00:00.000000', '13b9db74-e63f-4c50-a338-49a930cf52c4', 160, '6e65f32c-adde-4dc5-89e9-fd65dd2d5a0b', '2023-08-24 12:46:36.154551', '2023-10-06 12:13:00.498007', true, false, null, null, 'MIX');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('4d438056-ded8-42dc-8c0b-7cf1c43f49aa', '2c83734b-e42e-4ab0-acce-ba372b0e2744', '0eca508e-e3d0-486a-b708-a1b192842670', '2023-02-22 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 44, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-02-10 20:49:30.634746', '2023-11-03 19:21:13.776994', false, true, 'rrrr', 'aaa', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('fc36b526-456b-4063-a601-10214a44ae07', '001a8e97-069f-471c-bd80-dd168f5cdbfd', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-10-06 00:00:00.000000', null, 'e5ced154-40b8-433a-b24a-bedd6bd12ed7', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-10-06 12:13:32.863800', '2023-10-06 12:13:32.863810', false, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('9a7ddc61-8ef0-4d92-a319-3a4db135a26f', '52673af1-f94e-474d-ad03-58398421b92a', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-10-10 00:00:00.000000', '2023-10-13 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 12, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-10-30 12:56:14.105045', '2023-10-30 12:56:14.105055', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('f199d156-79eb-4c60-875e-9045a9f7d4d3', '52673af1-f94e-474d-ad03-58398421b92a', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-10-13 00:00:00.000000', '2023-10-30 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 123, 'a0d3a765-92af-4af1-94c5-d496d84a449e', '2023-10-30 12:56:48.959993', '2023-10-30 12:56:48.960002', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('8cee031f-3097-4732-b95e-4a49ba934f06', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2021-07-26 00:00:00.000000', '2023-02-22 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-01-19 21:02:00.495298', '2023-11-17 19:31:23.313178', false, false, 'benefits', null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('642fba67-b57a-4b42-96a8-baf0235a00f8', 'bf402384-44f1-49a7-b9e7-71b841b753c1', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-11-24 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 13, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-11-24 12:47:06.936429', '2023-11-24 12:59:18.085205', true, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('91bd49b9-f5a6-4b3c-b62e-74fdc69de7d6', 'd102b786-7f8a-42b4-ab49-30b8390ee173', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-09-15 00:00:00.000000', '2023-10-18 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 160, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-09-15 12:32:31.556601', '2023-12-14 14:20:10.442323', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('aa04175f-0be1-4299-a4f5-2e8080d38262', 'bf402384-44f1-49a7-b9e7-71b841b753c1', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-11-21 00:00:00.000000', null, '6d85225e-5d41-447e-aaac-7f11ce593c97', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-11-24 12:59:33.890335', '2023-11-28 14:28:41.551318', false, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('92464b4d-25be-41ab-bcc0-dda3473961f2', '03530cf8-d267-48a3-9c1b-f2dd47894e96', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-03-01 00:00:00.000000', '2024-02-02 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', 40, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-08-31 12:47:22.524662', '2024-02-03 09:00:00.312050', false, false, 'benefits2', 'notesrrrk', 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('1d5fcce5-998d-4cbd-80aa-63b21c045da8', 'bf402384-44f1-49a7-b9e7-71b841b753c1', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-11-20 00:00:00.000000', '2023-11-28 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-11-28 14:25:30.866383', '2023-11-28 14:28:41.548078', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('7ac2375b-c772-4326-920b-9dce729c46ee', '30af46d3-56a3-48a4-ac4f-1d4af6639b6c', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-11-30 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 29, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-11-30 12:45:15.700026', '2023-11-30 12:45:23.496744', true, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('acc0de87-a55e-4ece-bc32-ebb70d0b9eea', '30af46d3-56a3-48a4-ac4f-1d4af6639b6c', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-11-29 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-11-30 12:45:37.495400', '2023-11-30 12:45:43.316821', true, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('14ea396d-cf43-469a-b775-e94ee04b5802', '30af46d3-56a3-48a4-ac4f-1d4af6639b6c', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-01 00:00:00.000000', '2023-12-06 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'a0d3a765-92af-4af1-94c5-d496d84a449e', '2023-11-30 12:45:58.319883', '2023-12-06 13:29:22.069182', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('8accb2f3-965c-4aef-b8e9-b2cccc9010f1', 'b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-05 00:00:00.000000', '2023-12-08 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', null, 'b79b259d-92cf-4336-abae-007d92ac6a08', '2023-12-08 12:54:31.065262', '2023-12-08 12:55:09.147690', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('60826af4-45dc-4859-a222-dbd77f0ccff2', 'b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-09 00:00:00.000000', '2023-12-13 00:00:00.000000', '6d85225e-5d41-447e-aaac-7f11ce593c97', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-14 13:51:47.283592', '2023-12-14 14:20:10.467411', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('b58b2a4f-c3e7-4fb6-af75-dd340264d172', 'b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-14 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-14 13:53:02.319263', '2023-12-14 14:20:10.507331', false, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('5f66f58a-da9c-4fb0-97f7-cb78d380be46', 'f5a61455-0e7d-434c-b510-233b087726e2', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-06-01 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', 80, 'a8b328a1-d6f1-49f0-b712-a3fe7fcc29bf', '2023-12-22 14:57:00.295800', '2023-12-22 14:57:00.295827', false, true, null, null, 'TRAINEE');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('2b7002a2-b12b-430f-9f37-ff46a0683c4e', '09f38a52-e6fe-4f24-b39a-d1ec507eca61', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-26 00:00:00.000000', null, 'e90e374a-6770-4e0b-a194-2f6e68eee1f6', 160, '6e65f32c-adde-4dc5-89e9-fd65dd2d5a0b', '2023-12-21 17:59:13.466985', '2024-01-06 09:00:00.484243', false, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('c195dc81-6ba3-48a7-9c6b-7d3ff1f9c188', '8561055d-f978-4ca2-b706-742ad874b042', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-08 00:00:00.000000', null, '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-07 23:45:22.722317', '2024-01-06 17:44:25.320492', true, true, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('044a1d92-8ead-4307-b3b6-9e5d836e9316', '8561055d-f978-4ca2-b706-742ad874b042', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-04 00:00:00.000000', '2024-01-09 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-07 23:45:06.912953', '2024-01-09 15:23:43.393629', true, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('db221288-19bf-4192-b479-c6dff4a0d3d6', '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-04 00:00:00.000000', '2024-06-06 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-07 23:21:42.380189', '2024-06-06 13:13:20.426470', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.contract (id, employee_id, company_id, start_date, end_date, role_id, hours_per_month, seniority_id, created_at, modified_at, deleted, active, benefits, notes, contract_type) VALUES ('81ae3d1b-19cc-4adf-ad7f-cc6c24c66dfb', '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '6ae4758c-04f9-455d-9e54-eabc386d02c2', '2023-12-07 00:00:00.000000', '2024-06-06 00:00:00.000000', '461b6194-1870-4ac6-9a2e-db16375ae3d1', null, 'e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', '2023-12-07 23:23:16.488753', '2024-06-06 13:13:20.431489', false, false, null, null, 'CONTRACTOR');
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('bfa33035-5f9d-40c1-a669-2232f9baf726', 'Argentina', '2023-06-07 16:03:20.302635', '2023-06-07 16:03:20.302659', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('4580e808-a967-4485-8c50-2f759dd25ab1', 'Venezuela', '2023-06-07 16:03:39.051775', '2023-06-07 16:03:39.051787', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('4183db1d-759e-44ce-9e8d-8e20a0bc0017', 'ALL', '2023-12-18 12:17:58.449000', '2023-12-18 12:17:58.449000', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('a6e9e994-ca00-488c-b24b-b1b4533a1366', 'España', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('737e092f-5b9d-4cf6-b7fe-b52a8a30c46f', 'Ecuador', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('0f21a1d9-22c5-405c-aea0-70d50f75b215', 'Portugal', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('9de568ea-d862-4881-ab1c-d9816461f654', 'Italia', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('594ddf9b-4f9c-46ed-8503-0801e1ef02b1', 'Costa Rica', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('2a4bb151-7ef8-4aac-93d6-ed8005ed086f', 'Uruguay', '2024-01-05 13:00:39.325408', '2024-01-05 13:00:39.325408', false);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee', 'Colombia', '2023-06-07 16:03:32.357658', '2024-01-05 15:24:00.363348', true);
INSERT INTO public.country (id, name, created_at, modified_at, deleted) VALUES ('327ee741-b0f9-4502-a2ac-b2f61ec69c5c', 'China', '2024-02-16 14:50:50.907535', '2024-02-16 14:50:50.907552', false);
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('fd5b20f6-33e9-4180-8da5-c8db9a7c299b', 'Nicky', 'Jam', 'nick@gmal.com', null, null, null, null, null, null, '2024-01-05 13:36:44.123404', '2024-01-05 13:36:44.123418', false, '15', null, null, null, '16', null, null, null, null, true, '0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('a732a375-478b-44e0-bcde-937c2b166853', 'delete', 'emplo', 'aa@gmail.com', null, null, null, null, null, null, '2024-01-08 17:35:42.953604', '2024-01-08 17:35:51.272841', true, '16', null, null, null, '6', null, null, null, null, false, '4580e808-a967-4485-8c50-2f759dd25ab1');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('4646d754-b847-45da-a0c9-988a376016a9', 'Employee', 'TestDev2', 'test@gmail.com', null, null, null, null, null, '2000-12-12', '2024-01-09 15:05:01.330096', '2024-01-09 15:05:27.704989', false, '01', null, null, '01', '01', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('49484990-2dcf-4131-bacc-23a2e8e04916', 'Test', 'McTesterson', 'test@gmail.com', null, null, null, null, null, null, '2024-04-24 20:46:24.679641', '2024-04-24 20:46:24.679669', false, '123456', null, null, null, '12345789', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('61b9ec38-4fdb-4591-9626-0ea32383c814', 'Employee', 'Entropy', 'facundo@entropyteam.com', '4802019', 'address test', 'city test', 'state test', '111', '1997-10-10', '2023-02-03 12:53:55.056615', '2023-02-13 13:08:00.043536', true, '0905', 'aa', '111', '1234', '40533159', '3816893512', 'aa', 'aaaa', null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('1033a4f8-cc94-4979-903d-f4a2be8bc1b7', 'Tito', 'Rodriguez', 'tito@gmail.com', null, null, null, null, null, null, '2023-03-10 13:03:08.647013', '2023-08-23 20:22:29.492152', true, '123ABC', null, null, null, '27778998', null, null, null, 'tito@trabajo.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d7717471-984b-431c-859e-dea6c2d5c41f', 'Employee', '20', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:32:45.989137', '2023-08-23 20:22:29.494782', true, '20', null, null, null, '4454', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ca2ee956-4f9c-4f12-b406-697093d810a0', 'Sin', 'Address', 'aa@aa.com', '3123213', null, null, null, null, null, '2023-02-15 16:19:00.879317', '2023-04-13 19:49:49.493621', true, '001', null, null, '432432', '432432', '343242', null, null, 'aa@a', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('efcaf5b5-8e69-4a79-b4a0-12666f66b661', 'Messi', '22', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:35:23.039272', '2023-08-23 20:22:29.644150', true, '22', null, null, null, '4454', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ae28d926-b9c8-403f-a9e9-4a0bd8c7a93c', 'Employee', '21', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:34:38.971719', '2023-08-23 20:22:29.644837', true, '21', null, null, null, '23154', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('dcfaff32-829b-4bb0-83dc-d88aae057865', 'Employee', 'number 26', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:36:49.616521', '2023-08-23 20:22:29.499858', true, '26', null, null, null, '40533150', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d9690ba1-54c9-4408-8db3-791e576b1ed6', 'Employee', '19', 'employee@entropyteam.com', null, null, null, null, null, null, '2023-03-15 15:31:52.728020', '2023-08-23 20:22:29.508048', true, '19', null, null, null, '12345', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('36f7c26a-7142-43e9-8a6a-fb936c9ace88', 'Joaquin', 'Auday', 'test@mail.com', '497590', null, null, null, null, '1991-11-07', '2023-08-18 15:48:23.676915', '2023-08-18 15:51:47.312226', true, 'Joaquin711', null, null, null, '711', '987654321', null, null, 'test@labour.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d50a6216-655d-4114-8a77-628ea651294a', 'Messi', '24', 'employee@entropyteam.com', null, null, null, null, null, null, '2023-03-15 15:36:02.793472', '2023-08-23 20:22:29.380678', true, '24', null, null, null, '12345', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('178c3b32-8907-43ad-ada7-7c852c296b02', 'Messi', '16', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:30:37.707276', '2023-08-23 20:22:29.383342', true, '16', null, null, null, '4454', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('7ffec6f5-d8c5-47ab-90b5-959a0c294295', 'Messi', '18', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:32:24.544903', '2023-08-23 20:22:29.335796', true, '1234', null, null, null, '40533150', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('55cb4b82-4a90-48f0-9ad9-7576791baa10', 'Employee', 'No tech test', 'facundo@entropyteam.com', '3816893512', null, null, null, null, '2023-12-02', '2023-02-13 13:08:40.993148', '2023-08-23 20:22:29.384433', true, '1303', null, null, '123464', '12345', '4852019', null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('7643c2a7-64f2-432f-aaf7-0a3bb16323ad', 'Basic', 'Employee Test', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-02-22 15:37:31.223489', '2023-08-23 20:22:29.333223', true, '00111033', null, null, null, '40533159', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('cd4975c4-5300-41d9-9f96-58b0882358bb', 'jmghjmj', 'fhmfhjmj', 'mn@rt.com', null, null, null, null, null, null, '2023-02-22 18:23:51.678514', '2023-08-23 20:22:29.385853', true, '5255', null, null, null, '255456221', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('532627a7-621b-4da4-b7ee-717275b55b31', 'Federico', 'Almeida', 'federicoalmeida15@gmail.com', '03813035553', 'casa', 'casa', 'casa', '4107', '2000-10-16', '2023-07-07 16:15:32.920313', '2023-08-23 20:22:29.510151', true, 'Federico Almeida', 'Federico Almeida', null, '99', '99', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('6013b31c-e39d-4fed-97b4-c1db4f2e44b4', 'Employee', '14', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:29:47.960463', '2023-08-23 20:22:29.622339', true, '14', null, null, null, '23154', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d1be7f70-a6e7-4f7d-8384-ec55928b2362', 'Employee ', 'for delete', 'emp@gmail.com', null, null, null, null, null, null, '2024-01-05 15:20:12.097585', '2024-01-05 15:20:29.771904', true, '17', null, null, null, '18', null, null, null, null, false, '9de568ea-d862-4881-ab1c-d9816461f654');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('8b33e1bb-b6c7-45ee-96ab-34fc763237d1', 'Federico', 'Almeida', 'federicoalmeida15@gmail.com', '3813035553', 'Mi casa', 'Casa', 'Casa', '4107', '2000-10-16', '2023-07-07 16:14:46.825229', '2023-08-23 20:22:29.372099', true, 'Federico Almeida', null, null, '99', '99', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d32f4a7c-d2a9-4a05-9315-ce046669fc50', 'Employee', '15', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:30:08.875456', '2023-08-23 20:22:29.623174', true, '15', null, null, null, '23154', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('7570b0cc-34c9-43fd-96c0-e27c12717db4', 'dibu', '23', 'facundo@entropyteam.com', null, null, null, null, null, null, '2023-03-15 15:35:42.525556', '2023-08-23 20:22:29.632977', true, '23', null, null, null, '12345', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('b5f728c9-cc37-48ab-bfd7-0b341b83f264', 'Employee', 'Test 25', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:36:24.300060', '2023-08-23 20:22:29.634746', true, '25', null, null, null, '40533150', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ddebec77-528c-4a89-8b84-adb8fd5abcb0', 'Employee', '17', 'aaaa@gmail.com', null, null, null, null, null, null, '2023-03-15 15:31:02.510803', '2023-08-23 20:22:29.641598', true, '17', null, null, null, '40533159', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('c3d1e856-827d-40ee-b772-03e3dbbbf60a', 'test1', 'test1', 'aa@aa.com', null, null, null, null, null, null, '2023-06-29 21:42:36.373896', '2023-08-23 20:22:29.650129', true, '43', null, null, null, '32432432423', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', 'Test dev', 'User', 'aaa@sss.com', '43232432', 'fake street 123', 'Ciudad', 'Buenos Aires', '12345', '2020-03-19', '2023-04-17 14:32:32.180788', '2023-08-23 20:22:29.332609', true, '123456', null, null, '32131313', '32233322', '32423423', 'Nada', null, 'aaa@aaa.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('5fa672f5-420e-4737-b2e1-f50fd00120a2', 'Facundo', 'Orellana', 'facundo@entropyteam.com', '4802019', 'ecuador', 'sanmiguel', 'tucuman', '4000', '1997-05-09', '2023-02-03 12:49:18.471807', '2023-10-26 19:21:14.553251', false, '0509', 'aaaaaa', '111', '1234', '40533159', '3816893512', 'aa', 'aa', 'facu.orellana@dashsolutions.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('20e6eee0-abd5-401d-9ad1-ee2c4f84e408', 'Daniela', 'Jacquelin', 'dani@gmail.com', '11111', 'kfjfhdkj', 'Cordoba', 'Cordoba', '5000', '1983-06-05', '2022-12-02 13:10:47.279287', '2023-10-20 12:36:30.630242', false, '12', 'Daa Jaa', '35168877994', '5416546', '29733879', '11111', e'lorem ipsum now
later
and after that', 'Swiss Medical', 'daniela.jacquelin@entropy.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('5dea6ed4-63aa-434e-b50d-610b08df4843', 'Magali', 'Fernandez', 'magali@entropy.com', null, 'velez sarsfie', 'san miguel tucuman', 'Tucuman', '7000', '1990-04-04', '2023-01-02 19:19:31.875077', '2023-08-23 20:22:29.685136', true, 'E002', null, null, '2735662555', '3566254154', '35466484616', null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ecc3d69d-7d77-4bd2-badb-69dcd3b6b901', 'Federico', 'Almeida', 'federicoalmeida15@gmail.com', '2132131', 'Lima 2029', 'Yerba Buena', 'Tucumán', '4107', '2000-10-16', '2023-07-18 12:31:53.626210', '2023-08-23 20:22:29.639895', true, 'Federico Almeida', 'Federico Almeida', null, '123456', '13245', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', 'Test', 'User', 'aaa@sss.com', '1321321', null, null, null, null, '1991-06-05', '2023-02-23 12:36:48.757328', '2023-08-23 20:22:29.385286', true, '11001', null, null, '3213213', '32131312', '3432432', null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('ae92a483-e792-4f1d-8325-0baa07230d04', 'hfth', 'gzdzsg', 'danielajac@recon.com', '53453', 'hyythyt', 'yhtyhhy', 'ythyt', '587', '2000-02-22', '2023-02-10 12:37:33.316469', '2023-02-13 13:07:55.082344', true, '777', 'cscawswf', '25385435', '25297338795', '55545668', '35122255', '576867867', 'jcygdjy', null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('2c83734b-e42e-4ab0-acce-ba372b0e2744', 'Agustin', 'Bourgeois', 'agustin@agustin.com', '321321321', '25 de mayo', 'Tandil', 'Buenos Aires', '321323', '1991-05-06', '2023-02-10 20:47:59.397588', '2023-02-10 20:47:59.397597', false, '43', null, null, '432434324', '432432432', '32321321', 'aaaaa', null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', 'Facundo', 'Orellana', 'facundo@entropyteam.com', '4802019', 'ecuador 2803', 'sanmiguel', 'tucuman', '4000', '1997-05-09', '2023-02-02 13:47:04.090254', '2023-02-03 12:47:47.950450', true, '0509', 'aaaaaa', '12412', '132', '40533159', '3816893512', 'notes', 'aa', null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('638bd1e1-5e18-42a5-b2d1-ba1086713634', 'Ludovico', 'Martinez', 'ludo.martinez@entropy.com', '33165464789', 'sarmiento 8755', 'Cordoba', 'Cordoba', '5016', '1999-02-02', '2022-12-26 18:01:56.022391', '2022-12-26 18:01:56.022403', false, '051', 'Sofia Martinez', '332555466221', '27356669850', '35666985', '33255466621', null, 'Osde 310', null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('a7993838-a826-48e9-96e6-e6413ca2bcd9', 'Daniela', 'Jacquelin', 'danielajacquelincel@gmail.com', '', 'pontones', 'Cordoba', 'Cordoba', '5000', '2000-06-05', '2022-11-10 18:17:43.520458', '2022-12-02 13:09:12.371630', true, '0102', '', '', '27297338795', '3516160966', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('93dfe48d-1ffb-475a-8005-4bf357d73ede', 'Employee', 'for deletion', 'mail@gmail.com', null, null, null, null, null, '2024-01-12', '2024-01-05 15:22:24.329205', '2024-01-05 15:23:01.702722', true, '888', null, null, null, '888', null, null, null, null, false, 'a6e9e994-ca00-488c-b24b-b1b4533a1366');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('03530cf8-d267-48a3-9c1b-f2dd47894e96', 'Maxi', 'Ibañez', 'maxi@entropy.com', '5646498649844', 'fdfrfvgfg', 'San Miguel de Tucumanss', 'tucuman', '25516', '2000-07-07', '2022-12-12 17:54:46.336200', '2024-04-16 19:19:50.719354', false, '003', 'Rosario Martinez', '381245566910', '29788464655', '31646498649', null, 'holis9', 'Swiss Medical', 'maxi1@entropy.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('cfcbb961-947a-4dee-a7f0-3582de2cb6c7', 'Employee', 'full name test', 'email@entropyteam.com', null, null, null, null, null, null, '2023-03-15 14:53:17.118932', '2023-08-23 20:22:29.640720', true, '1503', null, null, null, '56842', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('52673af1-f94e-474d-ad03-58398421b92a', 'Test', 'Contract', 'test@contract.com', null, 'Fake street 123', null, null, null, '2024-01-05', '2023-10-30 12:55:31.275990', '2024-01-05 14:56:44.707242', false, '01235', null, null, null, '123456789', null, null, null, 'test@contract.com', true, '4580e808-a967-4485-8c50-2f759dd25ab1');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('f5a61455-0e7d-434c-b510-233b087726e2', 'Federico', 'Almeida', 'federicoalmeida15@gmail.com', '03813035553', 'Lima 2029', 'Yerba Buena', 'Tucumán', '4107', '2000-10-16', '2023-11-27 16:21:00.845251', '2024-01-09 15:24:38.766354', false, '20', 'Federico Almeida', null, null, '20', '03813035553', null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('4a5b381b-68b1-4bb0-bc82-6c5009a8926b', 'Absolutamente', 'Nelson', 'thisismyemail@email.com', null, null, null, null, null, '1999-12-08', '2023-12-06 13:13:59.104779', '2024-06-06 13:13:20.384190', false, '0057', null, null, null, '12321321321', null, null, null, 'other_email@email.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('001a8e97-069f-471c-bd80-dd168f5cdbfd', 'Romina', 'Ramon', 'romina.ramon@gmail.com', null, null, null, null, null, '2000-06-06', '2023-08-24 12:45:01.353735', '2023-08-24 12:45:01.353754', false, '556', null, null, '25-40987654-5', '40987654', '3516567877', null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('bf402384-44f1-49a7-b9e7-71b841b753c1', 'Vacation', 'Test', 'vac@gmail.com', null, null, null, null, null, null, '2023-11-17 15:29:45.449196', '2023-11-17 15:29:45.449209', false, '12', null, null, null, '12', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('1d9d987d-0e95-411a-9d1c-bd2a583f52c4', 'Employee', 'TestDev1', 'federicoalmeida15@gmail.com', null, null, null, null, null, '2000-11-29', '2023-11-28 15:56:40.847153', '2023-11-29 12:51:38.433333', true, '12372321', null, null, null, '12312321', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('494a7629-ce2d-4d5c-91a5-eb238cc611dc', 'Employee', 'TestDev02', 'test@gmail.com', null, null, null, null, null, '2000-11-30', '2023-11-29 12:52:37.236981', '2023-11-29 12:52:37.236996', false, '19283', null, null, null, '19182', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('327292a7-10e4-431c-85a3-d1b4254a85c8', 'Marcos', 'Tropibolti', 'marcos_tropibolti@email.com', null, null, null, null, null, '1999-12-05', '2023-12-06 13:15:50.566628', '2023-12-06 13:16:51.792878', true, '0058', null, null, null, '3424324324', null, null, null, 'labour_marcos@email.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('30af46d3-56a3-48a4-ac4f-1d4af6639b6c', 'Test', 'Contract', 'thisismyemail@email.com', null, null, null, null, null, '1995-11-29', '2023-11-30 12:44:38.653542', '2023-12-06 13:29:21.698949', true, '0056', null, null, null, '321321312312', null, null, null, 'thisismyemail@email.com', false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('f03bab93-5582-49f7-a04a-e1332f86361f', 'John', 'Frusciante', 'johnf@gmail.com', null, null, null, null, null, '1999-11-21', '2023-11-16 12:20:00.951260', '2023-12-06 13:46:11.715391', false, '10', null, null, null, '10', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', 'Sergio', 'Conypaifander', 'segio@email.com', null, null, null, null, null, '2023-12-08', '2023-12-08 12:54:07.617058', '2023-12-08 12:54:07.617078', false, '0061', null, null, null, '432432424', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('91af0026-2700-4e11-b727-602bc9c52491', 'Employee', 'TestDev', 'fede@gmail.com', null, null, null, null, null, '2000-12-15', '2023-12-13 14:43:19.134909', '2023-12-13 14:43:49.866867', true, '00001', null, null, null, '00001', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('09f38a52-e6fe-4f24-b39a-d1ec507eca61', 'Belen', 'Gamez', 'belen.gamez@gmail.com', 'k', 'impira 5000', 'cordoba', 'cordoba', '5000', '2010-07-21', '2023-12-21 17:57:45.727584', '2023-12-21 19:54:59.196334', false, '988', null, null, '27369987765', '36998776', null, null, null, null, true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('24005b6b-718e-4926-b9f5-ac35b5952a7b', 'Javier', 'Paez', 'javierpaez012899@gmail.com', null, 'Cangallo 811', 'San Miguel de Tucumán', 'Tucumán', 'T4000', '1999-01-28', '2023-11-13 14:38:50.602159', '2023-11-13 14:38:50.602175', false, '9', null, null, null, '9', null, null, null, 'javier@entropy.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d102b786-7f8a-42b4-ab49-30b8390ee173', 'Joaquin', 'Auday', 'jauday@teste.com', '+543815455669', 'camino', 'San Miguel de Tucuman', 'Tucuman', '8445', '2001-02-15', '2023-09-15 12:30:29.692849', '2023-09-15 13:13:37.014568', false, '998', null, null, '27385545567', '38554556', null, null, 'Swiss Medical', 'jauday@workstride.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('d69b19e6-b649-42a4-8009-022eb11d0615', 'Employee', 'Test2', 'empleado@gmail.com', null, null, null, null, null, '2000-11-28', '2023-11-27 17:14:47.293775', '2023-11-27 17:25:48.181443', true, '1223', null, null, null, '1223', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('271d5a83-7d7b-4243-9aee-60be476b0d89', 'Nicky', 'Niclaudio', 'nicky@email.com', null, null, null, null, null, '2000-12-04', '2023-12-06 13:25:24.625469', '2023-12-06 13:25:58.969050', false, '0059', null, null, null, '432432423423', null, null, null, 'labour_nicky@email.com', true, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('951d79ac-8db5-4700-9c26-0dfdc20e6697', 'TestEmployee', 'TestDev', 'testDev@gmail.com', null, null, null, null, null, '2000-12-08', '2023-12-06 13:24:51.934260', '2023-12-06 13:28:05.584256', true, '009', null, null, null, '009', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('569c2ed3-cf3b-416e-bd1f-8c448ff6e538', 'Employee', 'TestDev', 'fede@gmail.com', null, null, null, null, null, '2000-12-15', '2023-12-13 14:46:57.434922', '2023-12-13 14:47:20.338560', true, '00002', null, null, null, '00002', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('8561055d-f978-4ca2-b706-742ad874b042', 'TestEmployee', 'Dev', 'dev@gmail.com', null, null, null, null, null, '2000-12-10', '2023-12-06 13:33:23.232183', '2024-01-09 15:23:42.667945', true, '00010', null, null, null, '00010', null, null, null, null, false, 'a6e9e994-ca00-488c-b24b-b1b4533a1366');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('a362978c-d905-4e96-bd17-18696f214521', 'Employee', 'Test10', 'federicoalmeida15@gmail.com', null, null, null, null, null, '2000-11-29', '2023-11-27 17:50:22.222315', '2023-11-27 19:43:37.690340', true, '123456', null, null, null, '121234', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('a55a4ad2-d4b4-4cec-b136-09288e7359f7', 'Employee', 'TestDev1', 'federicoalmeida15@gmail.com', null, null, null, null, null, '2000-12-10', '2023-11-27 19:44:21.204939', '2023-11-28 15:55:13.756971', true, '987', null, null, null, '987', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee (id, first_name, last_name, personal_email, phone_number, address, city, state, zip, birth_date, created_at, modified_at, deleted, internal_id, emergency_contact_full_name, emergency_contact_phone, tax_id, personal_number, mobile_number, notes, health_insurance, labour_email, active, country_id) VALUES ('3bad80be-cf5d-42fd-a506-69498fa6674b', 'Señor', 'u', 'sanoru@entropy.com', null, 'calle 8', 'Rosario', 'Santa fe', '879846', null, '2023-03-30 17:43:30.308338', '2023-08-23 20:22:29.346780', true, '00255', null, null, '2765664646', '554546461', null, null, null, null, false, 'bfa33035-5f9d-40c1-a669-2232f9baf726');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('a7993838-a826-48e9-96e6-e6413ca2bcd9', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('a7993838-a826-48e9-96e6-e6413ca2bcd9', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('638bd1e1-5e18-42a5-b2d1-ba1086713634', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('5dea6ed4-63aa-434e-b50d-610b08df4843', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('5fa672f5-420e-4737-b2e1-f50fd00120a2', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('61b9ec38-4fdb-4591-9626-0ea32383c814', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('ae92a483-e792-4f1d-8325-0baa07230d04', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('2c83734b-e42e-4ab0-acce-ba372b0e2744', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('55cb4b82-4a90-48f0-9ad9-7576791baa10', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('ca2ee956-4f9c-4f12-b406-697093d810a0', 'a2a1659f-2b60-4717-9075-9145d25bd7c7');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('1033a4f8-cc94-4979-903d-f4a2be8bc1b7', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('cfcbb961-947a-4dee-a7f0-3582de2cb6c7', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('6013b31c-e39d-4fed-97b4-c1db4f2e44b4', '0ce4763c-dbaa-48c5-9018-b16c9f313335');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('d32f4a7c-d2a9-4a05-9315-ce046669fc50', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('178c3b32-8907-43ad-ada7-7c852c296b02', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('ddebec77-528c-4a89-8b84-adb8fd5abcb0', 'a2a1659f-2b60-4717-9075-9145d25bd7c7');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('d9690ba1-54c9-4408-8db3-791e576b1ed6', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('7ffec6f5-d8c5-47ab-90b5-959a0c294295', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('7ffec6f5-d8c5-47ab-90b5-959a0c294295', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('b5f728c9-cc37-48ab-bfd7-0b341b83f264', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('3bad80be-cf5d-42fd-a506-69498fa6674b', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('c3d1e856-827d-40ee-b772-03e3dbbbf60a', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('8b33e1bb-b6c7-45ee-96ab-34fc763237d1', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('532627a7-621b-4da4-b7ee-717275b55b31', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('ecc3d69d-7d77-4bd2-badb-69dcd3b6b901', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('03530cf8-d267-48a3-9c1b-f2dd47894e96', '13b9db74-e63f-4c50-a338-49a930cf52c4');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('36f7c26a-7142-43e9-8a6a-fb936c9ace88', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('001a8e97-069f-471c-bd80-dd168f5cdbfd', '13b9db74-e63f-4c50-a338-49a930cf52c4');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('d102b786-7f8a-42b4-ab49-30b8390ee173', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('52673af1-f94e-474d-ad03-58398421b92a', 'a2a1659f-2b60-4717-9075-9145d25bd7c7');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('24005b6b-718e-4926-b9f5-ac35b5952a7b', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('f03bab93-5582-49f7-a04a-e1332f86361f', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('f5a61455-0e7d-434c-b510-233b087726e2', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('d69b19e6-b649-42a4-8009-022eb11d0615', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('a362978c-d905-4e96-bd17-18696f214521', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('a55a4ad2-d4b4-4cec-b136-09288e7359f7', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('1d9d987d-0e95-411a-9d1c-bd2a583f52c4', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('494a7629-ce2d-4d5c-91a5-eb238cc611dc', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('30af46d3-56a3-48a4-ac4f-1d4af6639b6c', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('327292a7-10e4-431c-85a3-d1b4254a85c8', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('951d79ac-8db5-4700-9c26-0dfdc20e6697', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('271d5a83-7d7b-4243-9aee-60be476b0d89', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('8561055d-f978-4ca2-b706-742ad874b042', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '0ce4763c-dbaa-48c5-9018-b16c9f313335');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('09f38a52-e6fe-4f24-b39a-d1ec507eca61', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('09f38a52-e6fe-4f24-b39a-d1ec507eca61', '0ce4763c-dbaa-48c5-9018-b16c9f313335');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('fd5b20f6-33e9-4180-8da5-c8db9a7c299b', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('4646d754-b847-45da-a0c9-988a376016a9', '461b6194-1870-4ac6-9a2e-db16375ae3d1');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('03530cf8-d267-48a3-9c1b-f2dd47894e96', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('03530cf8-d267-48a3-9c1b-f2dd47894e96', 'e90e374a-6770-4e0b-a194-2f6e68eee1f6');
INSERT INTO public.employee_role (employee_id, role_id) VALUES ('49484990-2dcf-4131-bacc-23a2e8e04916', '6d85225e-5d41-447e-aaac-7f11ce593c97');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('bd5951d6-420f-4e41-b055-4f4c31db57ba', '507d096b-f82a-4c8e-9656-b595c0784561');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('5fa672f5-420e-4737-b2e1-f50fd00120a2', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('5fa672f5-420e-4737-b2e1-f50fd00120a2', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('5fa672f5-420e-4737-b2e1-f50fd00120a2', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('61b9ec38-4fdb-4591-9626-0ea32383c814', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ae92a483-e792-4f1d-8325-0baa07230d04', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('2c83734b-e42e-4ab0-acce-ba372b0e2744', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('2c83734b-e42e-4ab0-acce-ba372b0e2744', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('03530cf8-d267-48a3-9c1b-f2dd47894e96', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ca2ee956-4f9c-4f12-b406-697093d810a0', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', '5c636e7d-43f7-4192-99af-430a8f9aec9e');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fb7c10dd-1db0-4d5d-af2b-799d874e7729', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('1033a4f8-cc94-4979-903d-f4a2be8bc1b7', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('cfcbb961-947a-4dee-a7f0-3582de2cb6c7', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('6013b31c-e39d-4fed-97b4-c1db4f2e44b4', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('d32f4a7c-d2a9-4a05-9315-ce046669fc50', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('178c3b32-8907-43ad-ada7-7c852c296b02', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ddebec77-528c-4a89-8b84-adb8fd5abcb0', '5c636e7d-43f7-4192-99af-430a8f9aec9e');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('d9690ba1-54c9-4408-8db3-791e576b1ed6', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('7ffec6f5-d8c5-47ab-90b5-959a0c294295', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('c3d1e856-827d-40ee-b772-03e3dbbbf60a', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('8b33e1bb-b6c7-45ee-96ab-34fc763237d1', '5c636e7d-43f7-4192-99af-430a8f9aec9e');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('8b33e1bb-b6c7-45ee-96ab-34fc763237d1', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('532627a7-621b-4da4-b7ee-717275b55b31', '5c636e7d-43f7-4192-99af-430a8f9aec9e');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('532627a7-621b-4da4-b7ee-717275b55b31', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ecc3d69d-7d77-4bd2-badb-69dcd3b6b901', '5c636e7d-43f7-4192-99af-430a8f9aec9e');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('ecc3d69d-7d77-4bd2-badb-69dcd3b6b901', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('36f7c26a-7142-43e9-8a6a-fb936c9ace88', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('d102b786-7f8a-42b4-ab49-30b8390ee173', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('d102b786-7f8a-42b4-ab49-30b8390ee173', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('52673af1-f94e-474d-ad03-58398421b92a', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('24005b6b-718e-4926-b9f5-ac35b5952a7b', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('f03bab93-5582-49f7-a04a-e1332f86361f', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('f5a61455-0e7d-434c-b510-233b087726e2', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('d69b19e6-b649-42a4-8009-022eb11d0615', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('a362978c-d905-4e96-bd17-18696f214521', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('a55a4ad2-d4b4-4cec-b136-09288e7359f7', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('1d9d987d-0e95-411a-9d1c-bd2a583f52c4', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('494a7629-ce2d-4d5c-91a5-eb238cc611dc', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('30af46d3-56a3-48a4-ac4f-1d4af6639b6c', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '41960cc8-350e-49a1-bb01-cbacb7fd752d');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('327292a7-10e4-431c-85a3-d1b4254a85c8', '33444f84-9d1a-498b-9ee6-2657c79ad7c1');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('951d79ac-8db5-4700-9c26-0dfdc20e6697', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('271d5a83-7d7b-4243-9aee-60be476b0d89', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('8561055d-f978-4ca2-b706-742ad874b042', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('b7b147c0-bbc9-4e51-bdc8-d9613a78a3f4', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('fd5b20f6-33e9-4180-8da5-c8db9a7c299b', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('4646d754-b847-45da-a0c9-988a376016a9', '917aff98-a31b-4a5f-a0b8-dd938e54e20a');
INSERT INTO public.employee_technology (employee_id, technology_id) VALUES ('49484990-2dcf-4131-bacc-23a2e8e04916', 'f7dc87e7-dfed-4445-a29a-600d62380fd6');
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('39dcf29f-24ba-4515-afb2-a8447ecc9b0c', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-05-06 00:00:00.000000', 'Test previous holiday ', '2023-06-23 13:45:30.472111', '2023-07-07 15:34:20.155932', true);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('2cc0e10e-8685-4663-9075-4d0b12e981ac', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2024-07-30 00:00:00.000000', 'Test Holiday', '2023-07-07 15:09:08.841001', '2023-07-07 15:34:33.546127', true);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('ad71a2fb-dca7-46f7-ad06-2f72dcab875d', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-10-26 00:00:00.000000', 'saraza', '2023-08-24 12:50:47.637700', '2023-08-24 12:53:20.289582', true);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('ed8dd92f-0d66-422e-b310-9de1757ffafc', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-12-26 00:00:00.000000', 'Test holiday', '2023-06-23 13:44:00.650445', '2023-12-22 13:14:35.691371', true);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('6a7dd910-0f09-4c78-a595-9c0cb6acc9af', '0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee', '2024-01-05 00:00:00.000000', 'Holiday for colombia', '2024-01-05 13:33:40.667130', '2024-01-05 15:53:44.679366', true);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('ef69a276-dfc2-4cc7-a66d-bdd6c93dcde6', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2024-02-24 00:00:00.000000', 'new holiday', '2024-02-22 13:42:24.260687', '2024-02-22 13:42:24.260706', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('25088c23-7b21-4d07-83c3-a642ed11c6f3', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-06-07 00:00:00.000000', 'Holiday Test', '2023-06-07 16:04:02.038706', '2023-06-07 16:04:02.038719', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('4950eb2b-0f59-4cf1-a938-80b58f44cfcb', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-05-25 00:00:00.000000', 'New holiday', '2023-06-23 16:24:14.992865', '2023-06-23 16:24:14.992887', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('a36d9ba2-0760-4d51-991f-fbb74da4e4fd', '0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee', '2026-10-15 00:00:00.000000', 'Test', '2023-07-07 16:34:43.297006', '2023-07-07 16:34:43.297017', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('bc316e12-4bc7-4f1d-ac74-67838c7d914d', '0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee', '2023-08-30 00:00:00.000000', 'dia del amor', '2023-08-24 12:59:14.900489', '2023-08-24 12:59:14.900501', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('5d6094fa-3e19-4d66-bcc8-d82310516984', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2023-06-20 00:00:00.000000', 'Dia de la bandera', '2023-06-07 16:08:12.366694', '2023-10-08 23:13:16.271112', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('d4da97c7-f091-413d-8075-af480c45f8d8', '4183db1d-759e-44ce-9e8d-8e20a0bc0017', '2023-12-24 00:00:00.000000', 'Christmas', '2023-12-21 21:04:42.384681', '2023-12-21 21:04:42.385630', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('7bb034b1-ee68-402d-b23f-9e2bc7854607', '0fdc8814-ccc3-4458-8b6e-fb47a9d4f8ee', '2023-12-23 00:00:00.000000', 'Test', '2023-12-22 13:03:33.935066', '2023-12-22 13:04:05.778361', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('cfc43454-c427-48c7-a240-619e1ae6e29d', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2024-01-08 00:00:00.000000', 'holiday for argentina', '2024-01-05 13:34:14.942779', '2024-01-05 13:34:14.942799', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('9e9e1d6e-e53e-45a2-983b-d0eb6c52bb10', '4183db1d-759e-44ce-9e8d-8e20a0bc0017', '2024-01-04 00:00:00.000000', 'holiday for all', '2024-01-05 13:33:10.299992', '2024-01-05 13:34:29.893262', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('1058800a-cae9-42c9-8452-0687cdea2172', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2024-01-14 00:00:00.000000', 'Test holidayssss', '2023-09-14 14:09:00.761430', '2024-02-21 13:27:10.994097', false);
INSERT INTO public.holiday_calendar (id, country_id, date, description, created_at, modified_at, deleted) VALUES ('a866c0c7-8036-468e-9854-b42e7ca67c54', 'bfa33035-5f9d-40c1-a669-2232f9baf726', '2024-02-23 00:00:00.000000', 'Test Delete', '2024-02-22 14:11:25.746468', '2024-02-22 14:11:39.283590', true);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('300b4c36-376e-48c6-80ae-08024a4b51e7', 'Compensation', '2023-05-02 16:18:24.422029', '2023-05-02 16:18:24.422188', false);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('a5e0c183-d1ca-4252-9f8a-0c368617987f', 'Maternity leave', '2023-05-02 16:18:38.257383', '2023-05-02 16:18:47.339108', false);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('cbed2392-28c9-431a-9235-22d77d82eb56', 'Sickness', '2023-05-02 16:18:54.954002', '2023-05-02 16:18:54.954019', false);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('3440b93d-3cd2-4889-a2ba-ea1474cb9956', 'leave type to delete', '2023-05-02 16:19:06.868868', '2023-05-02 16:19:17.944314', true);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('3da58062-75c0-4fb1-96e5-257c2f77530e', 'Test', '2023-07-19 12:26:39.864650', '2023-07-19 12:26:39.864662', false);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('2c791ec8-0a87-41c5-8a2c-ce8285fa567a', 'Moving', '2023-11-17 19:32:11.875194', '2023-11-17 19:32:11.875206', false);
INSERT INTO public.leave_type (id, name, created_at, modified_at, deleted) VALUES ('9a9d6149-d328-4230-b4e8-5a641b39019a', 'Vacation', '2023-05-02 16:18:01.106029', '2023-11-21 22:19:01.866599', false);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('d2f7f370-cd1d-45a5-a299-b31f21e8fff1', '638bd1e1-5e18-42a5-b2d1-ba1086713634', 'Pioneer', 'nn', 'Ludo.Mar', '2022-12-26 18:01:56.027150', '2022-12-26 18:01:56.027158', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('7f2daf00-7b68-49a6-813a-8c9a58b3e2df', 'bd5951d6-420f-4e41-b055-4f4c31db57ba', 'Mercado Libre SRL', 'Argentina', 'facu.orellana7', '2023-02-02 13:47:04.095943', '2023-02-02 13:47:04.095956', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('309ef440-bb25-47dd-a9c8-443bf147cc64', '61b9ec38-4fdb-4591-9626-0ea32383c814', 'Banco macro', 'pruebaaa', 'aaaaa', '2023-02-03 12:53:55.059531', '2023-02-03 12:53:55.059544', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('f4b5c920-4c96-4c22-99ae-4201038f9d8f', '2c83734b-e42e-4ab0-acce-ba372b0e2744', 'aaaa', 'ars', '32132321', '2023-02-10 20:47:59.403850', '2023-02-10 20:47:59.403858', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('73055408-a559-4e9d-ab0e-1564ee23e39a', 'ebea4e42-4fc6-4b0d-a1f5-4db2602a3ea8', 'bru', 'Argentina', '3313133131', '2023-04-17 14:32:32.212172', '2023-04-17 14:32:32.212188', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('48329f7b-8ff5-48c0-a5ff-d5bdb82c1b20', '001a8e97-069f-471c-bd80-dd168f5cdbfd', 'mercado pago', 'Argentina', 'mp.rr', '2023-08-24 12:45:01.359133', '2023-08-24 12:45:01.359147', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('36d5a982-cc7a-4f24-8999-2b63ee2ed567', '001a8e97-069f-471c-bd80-dd168f5cdbfd', 'BNP', 'USA', '37475758793', '2023-08-24 12:45:01.361582', '2023-08-24 12:45:01.361596', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('f2be3a7c-efc2-4172-9fe0-4fd73e4b8566', 'd102b786-7f8a-42b4-ab49-30b8390ee173', 'BNP Paribas', 'USA', '56464688', '2023-09-15 12:30:29.725677', '2023-09-15 13:13:37.025466', false, '996645');
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('befddd66-4d9b-45cc-9d28-feaf49efa395', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', 'Nu', '', 'Dani.J', '2022-12-23 18:30:55.463754', '2023-10-20 12:36:30.672147', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('ff1d6192-8312-464a-af00-7f606c03e973', '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', 'Bank', 'Argentina', '164646498489498465999978978', '2022-12-23 18:30:13.398620', '2023-10-20 12:36:30.673440', false, null);
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('69e74441-406c-49cf-b8e1-69e6fb2e5018', '5fa672f5-420e-4737-b2e1-f50fd00120a2', 'paypal', 'Argentina', 'F.pay', '2023-10-26 19:21:14.614050', '2023-10-26 19:21:14.614064', false, '99988');
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('e0c9d64a-535a-4e65-8a29-90993c28455c', '5fa672f5-420e-4737-b2e1-f50fd00120a2', 'Mercado Libre SRL', 'Argentina', 'facu.orellana7', '2023-02-03 12:49:18.477161', '2023-10-26 19:21:14.619373', false, '948');
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('926d1aae-f4b2-4794-81c3-7f886e6c6f08', '09f38a52-e6fe-4f24-b39a-d1ec507eca61', 'banco', 'Argentina', 'Belen.Gamez', '2023-12-21 17:57:45.751277', '2023-12-21 19:54:59.201029', false, '');
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('a6bd6b0e-bf9c-4fe4-964f-e7df077ee2df', '09f38a52-e6fe-4f24-b39a-d1ec507eca61', 'Banco', 'USA', 'Belu.Gamez', '2023-12-21 17:57:45.754192', '2023-12-21 19:54:59.201910', false, '5566777888');
INSERT INTO public.payment_information (id, employee_id, platform, country, cbu, created_at, modified_at, deleted, routing_number) VALUES ('c512c5e7-3e4a-4ba7-a93a-c3a15fb2ee9c', '03530cf8-d267-48a3-9c1b-f2dd47894e96', 'Bank', 'Argentina', '5146546546516', '2022-12-26 16:09:40.604735', '2024-04-16 19:19:50.744120', false, null);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('5276a47c-bf08-48ca-8507-e1d95dbb7425', '737b721e-da6a-489c-80eb-fd4e54e3d380', 'ARS', 'HOUR', 1000, '2023-02-01 14:18:32.955472', '2023-02-01 14:25:19.756639', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('833bc0c3-d4a7-4139-9bf5-d192eaf6b9eb', 'a770e5d5-deaa-481e-9b47-ddccd1f6a6a9', 'USD', 'MONTHLY', 1200, '2023-02-01 14:17:18.870055', '2023-02-01 14:25:28.229588', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('738f014d-5693-44ef-bb13-d1a5da72ccd5', '834ebbee-b38d-4438-8160-0b7a9633881f', 'ARS', 'MONTHLY', 1000, '2023-02-02 13:48:27.398766', '2023-02-02 13:48:27.398775', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('e9bc939a-a4c6-4728-93c5-e0de2dc8062e', 'ca393d63-1d62-4b82-a707-295d9ec287ba', 'USD', 'HOUR', 1500, '2023-02-03 12:50:58.008913', '2023-02-03 12:50:58.008929', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('c8d9b568-b1eb-4ea9-82da-ed4516454b9f', '0d76701d-27e3-4098-a3df-a10d55b0b83b', 'ARS', 'MONTHLY', 1200, '2023-02-03 12:54:45.799861', '2023-02-03 12:54:45.799870', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('0453ca04-a5a9-4766-8013-a12cd32db264', '9cfbae59-e159-4622-bf44-9aeedce86be5', 'USD', 'HOUR', 1, '2023-02-10 20:48:52.056135', '2023-02-10 20:48:52.056150', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('6fa013f1-e741-4a1b-bccf-0e3825b5ac3c', '19e5c11d-6397-4a55-846a-4e3b03df0351', 'ARS', 'MONTHLY', 11111, '2023-02-27 15:58:34.970445', '2023-02-27 15:58:34.970455', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('bbc92499-5078-4e0d-93c9-90e189f7a858', '6f989f8d-7b15-4619-bc63-978d229c5587', 'USD', 'MONTHLY', 500, '2023-03-31 13:30:57.021352', '2023-03-31 13:30:57.021362', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('95be1f8e-f164-4982-a4b0-29dd0af9e38d', '6f989f8d-7b15-4619-bc63-978d229c5587', 'USD', 'HOUR', 100, '2023-03-31 13:30:57.025163', '2023-03-31 13:30:57.025170', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('3cb5d03a-f9f9-473a-a902-e7bbf23557b7', '16a4bf20-3424-40fc-ba06-a49774a31a16', 'USD', 'MONTHLY', 111, '2023-04-17 14:33:10.567525', '2023-04-17 14:33:10.567534', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('1f5fa13e-150b-40c9-bb73-e885e66d46b7', '259269ea-aae2-4763-8260-9628cf5d6e82', 'USD', 'HOUR', 1000, '2023-02-23 12:37:43.971463', '2023-08-09 13:51:48.989398', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('b7f2ea8f-439a-4166-9ead-bf3277bfd889', 'adb458bd-d0ec-4ef2-864f-fe50a4b16856', 'USD', 'MONTHLY', 3000, '2023-08-25 13:14:04.524236', '2023-08-25 13:14:04.524249', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('a184eafd-e3fe-4fb3-97f2-f75da7b58411', '75a20961-e4d3-4da2-831c-68d7e64da461', 'USD', 'MONTHLY', 1000, '2023-08-24 12:46:36.156954', '2023-08-28 16:01:41.927041', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('53ed0383-b20e-40bf-9dd2-6b724065af60', '75a20961-e4d3-4da2-831c-68d7e64da461', 'ARS', 'MONTHLY', 50000, '2023-08-24 12:46:36.159152', '2023-08-28 16:01:41.928879', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('49eb93e9-77e1-45c1-8fdf-2967475e719c', '92464b4d-25be-41ab-bcc0-dda3473961f2', 'USD', 'MONTHLY', 1500, '2023-02-02 13:43:35.332054', '2023-09-08 13:17:13.750428', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('421dc253-c46f-40cc-af1c-ae46c0c80850', '91bd49b9-f5a6-4b3c-b62e-74fdc69de7d6', 'USD', 'MONTHLY', 1000, '2023-09-15 12:32:31.563538', '2023-09-15 13:15:23.104517', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('b6b8b171-f56e-4bb7-b574-50286ca9c885', '91bd49b9-f5a6-4b3c-b62e-74fdc69de7d6', 'ARS', 'MONTHLY', 100000, '2023-09-15 12:32:31.567725', '2023-09-15 13:15:23.105374', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('02a3a046-30c9-4ec6-924f-2f79b1723a6f', 'fc36b526-456b-4063-a601-10214a44ae07', 'USD', 'MONTHLY', 123, '2023-10-06 12:13:32.866885', '2023-10-06 12:13:32.866893', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('d317e3cc-6188-4720-8c18-eba2a2a82013', '9a7ddc61-8ef0-4d92-a319-3a4db135a26f', 'USD', 'HOUR', 1233, '2023-10-30 12:56:14.108241', '2023-10-30 12:56:14.108248', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('fdf6aecd-fa42-4099-8dd4-e3b993178b6f', '4d438056-ded8-42dc-8c0b-7cf1c43f49aa', 'ARS', 'HOUR', 3, '2023-02-10 20:49:30.637610', '2023-11-03 19:21:13.779625', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('cd3ed0bd-42ef-489f-b2cb-57ebd65ee209', '2afd618f-a8e7-4c36-9299-98c528711fe0', 'USD', 'HOUR', 1500, '2023-11-16 12:20:44.925186', '2023-11-16 12:20:44.925196', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('a5b3a9dc-965e-419c-b6b4-15d7f73c455b', '2b7002a2-b12b-430f-9f37-ff46a0683c4e', 'USD', 'MONTHLY', 3000, '2023-12-21 17:59:13.471163', '2023-12-21 17:59:13.471171', false);
INSERT INTO public.payment_settlement (id, contract_id, currency, modality, salary, created_at, modified_at, deleted) VALUES ('d0a611df-c069-4c3e-99a8-ad3c4b40584a', '54680f2e-5e87-4870-835f-581b8e53dd9d', 'ARS', 'MONTHLY', 100000, '2023-11-13 14:39:32.840151', '2023-12-22 15:53:39.467367', false);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('ee5cfaf1-dd9a-4ee8-bac4-79858b062ec5', '4acb3450-e1af-40d6-8efe-20ae6fecfa5f', '286a166a-d628-43f4-b179-7d927c5f9a6c', 'Project 1', '2022-12-29 00:00:00.000000', null, null, '2022-12-29 18:40:18.003693', '2022-12-29 18:40:18.003704', false);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('38e96879-a7cc-44a4-94b6-2225d1c853f2', '1e86fc8c-bd47-41e6-b3f0-25a137490062', '286a166a-d628-43f4-b179-7d927c5f9a6c', 'checked', '2023-01-19 00:00:00.000000', null, null, '2023-01-19 20:17:13.182640', '2023-01-19 20:17:13.182650', false);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('8aa951ed-8020-4dee-b03b-7ab458b55a00', '4acb3450-e1af-40d6-8efe-20ae6fecfa5f', 'aa1ce125-33bd-4700-a3fb-5fbb4c69afbf', 'This is an awesome project', '2023-02-08 00:00:00.000000', null, 'Nada', '2023-02-10 20:43:39.845275', '2023-02-10 20:43:39.845287', false);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('d4748178-362b-413d-be80-ea4f4785e984', '4acb3450-e1af-40d6-8efe-20ae6fecfa5f', null, 'No project type test', null, null, null, '2023-02-22 15:24:48.750033', '2023-08-24 13:00:56.897431', true);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('d87cedcb-9c93-44f1-8bd9-4ffe3dbcc040', '4acb3450-e1af-40d6-8efe-20ae6fecfa5f', null, 'My new proyect', '2023-02-03 00:00:00.000000', null, null, '2023-02-23 12:45:41.252476', '2023-08-24 13:00:56.935725', true);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('058c663f-0d9c-4386-b6a8-23625fcd58df', 'fd347811-4f77-4639-a55b-515beb213a86', null, 'new project no project type test', null, null, null, '2023-02-27 15:36:35.181152', '2023-08-24 13:00:56.936377', true);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('46e19286-d582-4963-99f3-fd0b215ccc03', '6aee54c6-044c-4a4f-9575-65da2abee387', '286a166a-d628-43f4-b179-7d927c5f9a6c', 'Cool project', '2024-01-22 00:00:00.000000', null, null, '2024-01-22 13:17:23.812071', '2024-01-22 13:17:23.812082', false);
INSERT INTO public.project (id, client_id, project_type_id, name, start_date, end_date, notes, created_at, modified_at, deleted) VALUES ('f9ef157f-e7ee-4110-90ec-6d23aa47f66c', 'c3581ab5-dd05-49a1-a93e-2c94edd7b425', '286a166a-d628-43f4-b179-7d927c5f9a6c', 'Cool project', '2024-01-22 00:00:00.000000', null, null, '2024-01-22 13:17:40.762942', '2024-01-22 13:17:40.762954', false);
INSERT INTO public.project_type (id, name, created_at, modified_at, deleted) VALUES ('286a166a-d628-43f4-b179-7d927c5f9a6c', 'Fix price', '2022-11-10 18:36:30.702087', '2022-11-10 18:36:30.702102', false);
INSERT INTO public.project_type (id, name, created_at, modified_at, deleted) VALUES ('90c76453-1b6b-4928-9df4-5af8ec47f101', 'Marketing type', '2022-12-29 15:07:07.229083', '2022-12-29 15:08:40.617270', false);
INSERT INTO public.project_type (id, name, created_at, modified_at, deleted) VALUES ('aa1ce125-33bd-4700-a3fb-5fbb4c69afbf', 'Staff augmentation', '2023-02-10 20:42:56.428338', '2023-02-10 20:42:56.428348', false);
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('9f383c20-bcbc-4259-8bcd-2864c1b444a8', '2023-07-19 12:27:38.136541', '2023-07-19 12:27:38.136554', false, '2023-07-28 00:00:00.000000', '2023-07-28 00:00:00.000000', 'APPROVED', 'set 4 hours', 0.00, 4, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('fd2e6f24-3ce6-4ed0-821d-149bc9d69bac', '2023-07-25 14:28:43.562653', '2023-07-25 14:29:06.592804', true, '2023-08-02 00:00:00.000000', '2023-08-09 00:00:00.000000', 'APPROVED', null, 6.00, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('411ed661-cf52-491f-a8db-85ee727a0314', '2023-07-26 13:58:28.869614', '2023-07-26 13:58:28.869625', false, '2023-07-24 00:00:00.000000', '2023-07-25 00:00:00.000000', 'APPROVED', 'test past date', 2.00, 0, '638bd1e1-5e18-42a5-b2d1-ba1086713634', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('32272644-6e24-4b6f-9c50-594132bfee0f', '2023-07-20 16:09:11.539945', '2023-07-26 16:02:12.945825', true, '2023-07-26 00:00:00.000000', '2023-07-26 00:00:00.000000', 'APPROVED', 'tests', 0.00, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('3ecb588f-236c-4130-a693-34276aeb646d', '2023-07-26 15:56:59.419215', '2023-07-26 16:20:46.981234', false, '2023-07-26 00:00:00.000000', '2023-08-01 00:00:00.000000', 'APPROVED', null, 5.00, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('2215ae0f-cebf-466d-9e6e-5c5cd8082d21', '2023-07-26 13:57:46.134697', '2023-07-26 16:20:54.326159', true, '2023-07-28 00:00:00.000000', '2023-07-29 00:00:00.000000', 'APPROVED', 'Vacations', 1.00, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('b7347de3-7d3c-482f-ad53-36db4794e1da', '2023-07-19 12:22:17.068623', '2023-08-24 12:34:41.409519', true, '2023-07-19 00:00:00.000000', '2023-08-04 00:00:00.000000', 'APPROVED', 'Vacations', 13.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('7ca5f934-1e04-4593-a0e5-9f91d0e299c7', '2023-07-19 12:24:13.873174', '2023-08-24 12:34:58.623644', true, '2023-09-20 00:00:00.000000', '2023-10-05 00:00:00.000000', 'APPROVED', 'Vacations', 12.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('89223a76-ab73-4e49-8460-4c6fc0b735ce', '2023-08-24 12:48:55.398581', '2023-08-24 12:51:12.612127', true, '2023-10-25 00:00:00.000000', '2023-10-27 00:00:00.000000', 'APPROVED', null, 3.00, 0, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ee650b70-4b25-47f4-bbf0-bc1460b7a936', '2023-08-24 12:52:43.041394', '2023-08-24 12:53:07.277578', true, '2023-10-24 00:00:00.000000', '2023-10-27 00:00:00.000000', 'APPROVED', null, 3.00, 0, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('533fd529-5f80-4f8e-90ee-2111a5e24ec4', '2023-08-24 12:54:02.805777', '2023-08-24 12:54:02.805789', false, '2023-09-14 00:00:00.000000', '2023-09-15 00:00:00.000000', 'APPROVED', null, 2.00, 0, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('743f099c-1884-4587-8510-bab68d648cb8', '2023-09-15 12:38:49.876266', '2023-09-15 12:38:49.876274', false, '2023-09-25 00:00:00.000000', '2023-09-27 00:00:00.000000', 'APPROVED', null, 3.00, 0, 'd102b786-7f8a-42b4-ab49-30b8390ee173', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('e0915d40-f729-422a-b18c-6d3a3bef037b', '2023-09-15 12:39:28.362877', '2023-09-15 12:39:28.362889', false, '2023-09-15 00:00:00.000000', '2023-09-15 00:00:00.000000', 'APPROVED', null, 1.00, 0, 'd102b786-7f8a-42b4-ab49-30b8390ee173', 'cbed2392-28c9-431a-9235-22d77d82eb56');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('5653f5dd-7186-4593-8be4-2075b8a300ff', '2023-09-15 13:26:55.714019', '2023-09-15 13:27:25.913033', true, '2023-10-30 00:00:00.000000', '2023-11-03 00:00:00.000000', 'APPROVED', null, 5.00, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('2b03791e-15dc-47c4-b33c-d18b43837db9', '2023-10-19 14:54:18.739906', '2023-10-19 14:54:18.739916', false, '2023-10-26 00:00:00.000000', '2023-10-26 00:00:00.000000', 'APPROVED', null, 0.00, 4, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('b3f5d5df-69d1-4dda-b385-dfd9d8ed1c8f', '2023-10-19 14:56:58.453258', '2023-10-19 14:56:58.453269', false, '2023-10-20 00:00:00.000000', '2023-10-20 00:00:00.000000', 'APPROVED', null, 1.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('2ebf8740-6d0c-4fc5-89c3-0909300b30c2', '2023-10-20 13:20:32.339325', '2023-10-20 13:20:32.339342', false, '2023-10-23 00:00:00.000000', '2023-10-23 00:00:00.000000', 'APPROVED', null, 0.00, 4, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('e1237a4e-6262-4d7a-9120-b6d6f154de2a', '2023-10-20 13:21:26.479343', '2023-10-20 13:21:26.479355', false, '2023-10-23 00:00:00.000000', '2023-10-23 00:00:00.000000', 'APPROVED', null, 0.00, 4, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('8b67feef-fa01-49d7-bc10-65f81d66d267', '2023-11-02 13:12:01.386553', '2023-11-02 13:12:01.386569', false, '2023-11-23 00:00:00.000000', '2023-11-25 00:00:00.000000', 'APPROVED', null, 2.00, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ab886b00-0f7e-4744-8a60-da5f4fb81edc', '2023-11-03 14:32:41.019586', '2023-11-03 14:32:41.019603', false, '2023-11-10 00:00:00.000000', '2023-11-10 00:00:00.000000', 'APPROVED', 'test half day off', 0.50, 0, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('f4f9190a-bccf-4948-b9ec-27c6a1a4fbbd', '2023-07-19 12:23:27.275443', '2023-11-07 19:07:03.962601', true, '2023-08-15 00:00:00.000000', '2023-08-15 00:00:00.000000', 'APPROVED', 'compensation day', 1.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ff660dd1-5873-40f0-bde7-b2aa7d0ae69d', '2023-07-19 12:27:15.557795', '2023-11-07 19:07:25.500449', true, '2023-07-21 00:00:00.000000', '2023-07-21 00:00:00.000000', 'APPROVED', 'test new pto', 1.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('d096bb78-71e5-4880-ac07-3435b10daa1f', '2023-11-06 13:17:37.587663', '2023-11-07 19:07:43.078139', true, '2023-11-13 00:00:00.000000', '2023-11-13 00:00:00.000000', 'APPROVED', null, 0.50, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', 'cbed2392-28c9-431a-9235-22d77d82eb56');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('cbd3974d-deea-42a3-9c05-b329dfa56fff', '2023-10-19 14:52:47.752722', '2023-11-07 19:08:02.810833', true, '2023-10-25 00:00:00.000000', '2023-10-27 00:00:00.000000', 'APPROVED', 'test', 3.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('add1fb9b-c1b1-4e92-8422-301a8db6227c', '2023-11-07 19:17:52.188781', '2023-11-07 19:17:52.188788', false, '2023-11-15 00:00:00.000000', '2023-11-22 00:00:00.000000', 'APPROVED', null, 6.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('116bfa37-833f-4031-b2b1-1a9345304ae3', '2023-11-07 19:18:36.412246', '2023-11-07 19:18:44.378193', false, '2023-11-23 00:00:00.000000', '2023-11-23 00:00:00.000000', 'APPROVED', null, 1.00, 0, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('a1463850-aaf7-4045-863b-b893f70332ce', '2023-10-27 13:24:05.816735', '2023-11-15 18:07:12.605020', true, '2023-10-30 00:00:00.000000', '2023-10-30 00:00:00.000000', 'APPROVED', null, 1.00, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ee8caefd-e2cb-40f0-a00f-8aa87f520e95', '2023-10-19 14:24:03.705874', '2023-11-15 18:09:05.676088', true, '2023-10-20 00:00:00.000000', '2023-10-20 00:00:00.000000', 'APPROVED', 'test', 0.00, 2, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ac92e48c-fe3a-4b90-97f3-a8466b988ed9', '2023-11-17 14:47:39.815185', '2023-11-17 14:47:39.815192', false, '2023-11-17 00:00:00.000000', '2023-11-24 00:00:00.000000', 'APPROVED', null, 6.00, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('7eb434b6-2578-4e3f-85ba-692c6401cdc0', '2023-11-17 15:30:09.762101', '2023-11-17 15:30:56.452786', true, '2023-11-13 00:00:00.000000', '2023-11-17 00:00:00.000000', 'APPROVED', null, 5.00, 0, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('a23035e5-efde-45f0-86c1-44573b5b3fc5', '2023-11-17 15:31:22.499588', '2023-11-17 15:31:54.542236', true, '2023-11-15 00:00:00.000000', '2023-11-20 00:00:00.000000', 'APPROVED', null, 4.00, 0, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('90cb664b-8ac0-4df7-ba34-af409ffbb0eb', '2023-11-17 19:30:35.165394', '2023-11-17 19:30:35.165410', false, '2023-11-01 00:00:00.000000', '2023-11-03 00:00:00.000000', 'APPROVED', null, 3.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('e43b6cb7-0b7e-4321-b9d7-de1fbee92745', '2023-11-17 19:32:55.621619', '2023-11-17 19:34:21.076172', true, '2023-06-07 00:00:00.000000', '2023-07-26 00:00:00.000000', 'APPROVED', null, 33.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2c791ec8-0a87-41c5-8a2c-ce8285fa567a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('6d69035f-90d9-414b-9543-b74eb5b2f7b3', '2023-11-17 19:38:29.005427', '2023-11-17 19:38:29.005439', false, '2023-11-01 00:00:00.000000', '2023-11-03 00:00:00.000000', 'APPROVED', null, 3.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('26765081-b524-40aa-9e4f-15fdea64e620', '2023-10-19 14:25:03.717351', '2023-11-17 19:39:53.351376', false, '2023-10-23 00:00:00.000000', '2023-10-23 00:00:00.000000', 'APPROVED', 'h', 1.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('b0ef0144-4118-400c-b02a-d4fe4d2d7138', '2023-11-17 19:40:54.606438', '2023-11-17 19:41:13.994809', false, '2023-08-01 00:00:00.000000', '2023-08-03 00:00:00.000000', 'APPROVED', null, 3.00, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', 'cbed2392-28c9-431a-9235-22d77d82eb56');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('41b568dd-30a1-4c68-90b2-981ae9ca33d7', '2023-11-21 22:00:28.572814', '2023-11-21 22:17:11.184968', true, '2023-11-23 00:00:00.000000', '2023-11-27 00:00:00.000000', 'APPROVED', null, 3.00, 0, '52673af1-f94e-474d-ad03-58398421b92a', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('ba196aaa-9d40-459b-9936-55cdf38e3dba', '2023-11-21 22:18:19.498821', '2023-11-21 22:18:19.498831', false, '2023-11-22 00:00:00.000000', '2023-11-24 00:00:00.000000', 'APPROVED', null, 3.00, 0, '52673af1-f94e-474d-ad03-58398421b92a', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('6c160a39-e56f-41a8-baa0-296eee5084cf', '2023-11-24 13:36:33.005185', '2023-11-24 13:36:45.644839', true, '2023-02-01 00:00:00.000000', '2023-02-05 00:00:00.000000', 'APPROVED', null, 3.00, 0, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('9d7e39e5-61ce-4db8-b6d7-3ea989cb7974', '2023-12-01 12:43:26.643773', '2023-12-01 12:43:26.643793', false, '2023-12-02 00:00:00.000000', '2023-12-04 00:00:00.000000', 'APPROVED', 'test', 1.00, 0, 'd102b786-7f8a-42b4-ab49-30b8390ee173', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('f0c3ffce-62d0-4510-ae59-510555d481fa', '2023-12-06 13:34:26.698353', '2023-12-06 13:34:41.740222', true, '2023-12-06 00:00:00.000000', '2023-12-08 00:00:00.000000', 'APPROVED', 'Test Agustin', 3.00, 0, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('651250f6-5383-4d8d-86f7-7810b112a80c', '2023-08-23 20:24:34.497206', '2023-12-06 13:35:53.800464', false, '2023-09-01 00:00:00.000000', '2023-09-08 00:00:00.000000', 'APPROVED', 'ddd', 6.00, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('2235147e-a6ec-4e12-9017-24ba9c0478a0', '2023-12-06 13:37:34.999006', '2023-12-06 13:37:34.999021', false, '2023-12-13 00:00:00.000000', '2023-12-15 00:00:00.000000', 'APPROVED', null, 3.00, 0, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('3a772f81-cf45-435e-9bd8-09c2f37d37ef', '2023-12-06 13:30:07.584561', '2023-12-06 13:33:50.680395', true, '2023-11-29 00:00:00.000000', '2023-12-06 00:00:00.000000', 'APPROVED', 'Test Agustin', 6.00, 0, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('b06a06ff-639f-4014-abc0-04f87cfb7e73', '2023-08-24 12:53:40.035324', '2023-12-13 13:12:24.102433', false, '2023-10-23 00:00:00.000000', '2023-10-27 00:00:00.000000', 'APPROVED', null, 5.00, 0, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('a761f2ae-b6b1-4a20-91ef-cdb87d9be193', '2023-12-13 16:06:10.948138', '2023-12-13 16:06:10.948155', false, '2023-12-11 00:00:00.000000', '2023-12-13 00:00:00.000000', 'APPROVED', null, 3.00, 0, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('c28dca23-fade-468b-881c-b241b5f5a301', '2023-12-13 16:39:10.465992', '2023-12-13 16:39:10.466007', false, '2023-12-12 00:00:00.000000', '2023-12-13 00:00:00.000000', 'APPROVED', null, 2.00, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('f4e7741d-42e3-4dfe-86ca-6062bee4a0af', '2023-12-13 17:32:25.062036', '2023-12-13 17:34:06.438555', true, '2023-12-14 00:00:00.000000', '2023-12-16 00:00:00.000000', 'APPROVED', 'Test', 2.00, 0, '8561055d-f978-4ca2-b706-742ad874b042', '3da58062-75c0-4fb1-96e5-257c2f77530e');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('b1f7a558-3ee1-4725-b3c9-30fa3db451d4', '2023-12-14 13:51:18.171855', '2023-12-14 13:58:28.553044', true, '2023-12-15 00:00:00.000000', '2023-12-16 00:00:00.000000', 'APPROVED', 'Test', 1.00, 0, 'f5a61455-0e7d-434c-b510-233b087726e2', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('3f720075-b71f-468f-8eb6-fd0d62021eb1', '2024-01-05 13:37:39.741739', '2024-01-05 13:37:39.741756', false, '2024-01-03 00:00:00.000000', '2024-01-09 00:00:00.000000', 'APPROVED', null, 3.00, 0, 'fd5b20f6-33e9-4180-8da5-c8db9a7c299b', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('fa66613c-a901-42f1-9819-0d6395fabab9', '2023-12-21 19:54:40.831955', '2023-12-22 12:49:24.775710', false, '2024-01-08 00:00:00.000000', '2024-01-12 00:00:00.000000', 'APPROVED', 'jh', 5.00, 0, '09f38a52-e6fe-4f24-b39a-d1ec507eca61', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('a26a19ad-e5c3-494e-ae44-46020c90caf7', '2024-01-05 14:49:32.301623', '2024-01-05 14:50:11.409160', false, '2024-01-01 00:00:00.000000', '2024-01-03 00:00:00.000000', 'CANCELLED', null, 3.00, 0, 'f5a61455-0e7d-434c-b510-233b087726e2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('67a3065f-85d8-47a6-b000-080c39c1b65b', '2024-01-05 14:50:54.490357', '2024-01-05 14:51:03.139896', false, '2024-01-02 00:00:00.000000', '2024-01-02 00:00:00.000000', 'CANCELLED', null, 1.00, 0, 'f5a61455-0e7d-434c-b510-233b087726e2', 'cbed2392-28c9-431a-9235-22d77d82eb56');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('4f74e9e9-eb2b-4d8a-8308-ab30b9483c4a', '2024-01-05 14:52:33.304552', '2024-01-05 14:53:38.920029', false, '2024-01-02 00:00:00.000000', '2024-01-10 00:00:00.000000', 'CANCELLED', null, 5.00, 0, 'f5a61455-0e7d-434c-b510-233b087726e2', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('7b7631ca-a73e-4898-8af0-0368913856ac', '2024-01-05 15:00:54.354547', '2024-01-05 15:00:54.354560', false, '2024-01-03 00:00:00.000000', '2024-01-09 00:00:00.000000', 'APPROVED', null, 3.00, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('e742c35d-d94f-4edf-84c9-a8aa4bc96761', '2024-02-14 12:52:05.401830', '2024-02-14 12:52:05.401846', false, '2024-02-13 00:00:00.000000', '2024-02-13 00:00:00.000000', 'APPROVED', null, 1.00, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('19be04c8-295c-4fd7-bc21-6b9ba73f4aea', '2024-02-14 12:52:21.646314', '2024-02-14 12:52:21.646327', false, '2024-02-07 00:00:00.000000', '2024-02-07 00:00:00.000000', 'APPROVED', null, 0.50, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('1ba6ee2a-03cb-4227-83b7-981f846a3141', '2024-02-26 12:25:43.505376', '2024-02-26 12:25:43.505400', false, '2024-02-28 00:00:00.000000', '2024-02-28 00:00:00.000000', 'APPROVED', null, 0.50, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '300b4c36-376e-48c6-80ae-08024a4b51e7');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('d60b92d4-7a87-4490-a024-363321cb58aa', '2024-03-21 20:38:53.190007', '2024-03-21 20:38:53.190018', false, '2024-03-28 00:00:00.000000', '2024-04-02 00:00:00.000000', 'APPROVED', null, 4.00, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('f7b8bc00-a62e-41cc-ab6a-fff54e6c0f7c', '2024-04-24 20:47:54.524659', '2024-04-24 21:02:21.422806', false, '2023-04-24 00:00:00.000000', '2023-04-25 00:00:00.000000', 'CANCELLED', null, 2.00, 0, '49484990-2dcf-4131-bacc-23a2e8e04916', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('92d6d9a6-f4cc-4e67-8f18-d74ec06a4f53', '2024-04-24 21:04:58.450106', '2024-04-24 21:04:58.450120', false, '2024-04-24 00:00:00.000000', '2024-04-25 00:00:00.000000', 'APPROVED', null, 2.00, 0, '49484990-2dcf-4131-bacc-23a2e8e04916', 'cbed2392-28c9-431a-9235-22d77d82eb56');
INSERT INTO public.pto (id, created_at, modified_at, deleted, start_date, end_date, status, details, days, labour_hours, employee_id, leave_type_id) VALUES ('456c1df2-8aad-4454-a52b-3cbcafabfbfe', '2024-04-24 21:05:38.473827', '2024-04-24 21:05:38.473841', false, '2023-09-04 00:00:00.000000', '2023-09-05 00:00:00.000000', 'APPROVED', null, 2.00, 0, '49484990-2dcf-4131-bacc-23a2e8e04916', '9a9d6149-d328-4230-b4e8-5a641b39019a');
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('461b6194-1870-4ac6-9a2e-db16375ae3d1', 'Backend', '2022-11-08 16:41:50.786365', '2022-11-08 16:41:50.786387', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('6d85225e-5d41-447e-aaac-7f11ce593c97', 'Frontend', '2022-11-08 16:42:07.285319', '2022-11-08 16:42:07.285332', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('e90e374a-6770-4e0b-a194-2f6e68eee1f6', 'Product', '2022-12-23 18:33:17.458596', '2022-12-23 18:33:17.458607', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('0ce4763c-dbaa-48c5-9018-b16c9f313335', 'Analyst', '2023-01-03 13:58:44.067291', '2023-01-03 13:58:44.067311', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('e98005cf-41d7-4dfe-b261-26dfa6bdd678', 'test', '2023-07-12 18:57:37.180620', '2023-07-12 18:57:42.376226', true);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('9ba9e4bb-ddb6-4cde-8414-b526e69ef2d1', 'test1', '2023-07-12 18:58:12.504024', '2023-07-12 18:58:21.837141', true);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('13b9db74-e63f-4c50-a338-49a930cf52c4', 'RRHH', '2023-08-09 14:53:39.571372', '2023-08-09 14:53:39.571388', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('a2a1659f-2b60-4717-9075-9145d25bd7c7', 'UX', '2023-02-10 20:42:23.028977', '2023-08-24 13:01:39.508186', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('458c0c8b-8381-41f7-976e-174dcfbc5751', 'HR', '2023-08-28 15:42:06.577598', '2023-08-28 15:42:06.577624', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('e5ced154-40b8-433a-b24a-bedd6bd12ed7', 'HR2', '2023-10-05 13:00:30.383522', '2023-10-05 13:00:30.383536', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('19779657-0acd-4477-beb2-5c5153d7769b', 'Testing', '2024-04-16 19:11:49.396762', '2024-04-16 19:11:49.396779', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('e7ce0953-99b8-4636-8525-569848d256e7', 'Service Delivery Manager', '2024-04-16 19:13:41.431674', '2024-04-16 19:13:41.432121', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('53960cc1-0a5b-43af-a50e-9e5fc0468307', 'Test 2', '2024-04-16 19:14:29.288148', '2024-04-16 19:14:29.288158', false);
INSERT INTO public.role (id, name, created_at, modified_at, deleted) VALUES ('44983e92-87d3-48fc-8d80-992dcb112b58', 'Scrum master', '2024-05-10 12:03:11.084269', '2024-05-10 12:03:11.084285', false);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('e6ffa4e0-5961-4fb8-9667-ee55712c7d7c', 'Junior', '2022-11-10 18:36:12.260520', '2022-11-10 18:36:12.260536', false, 10);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('b79b259d-92cf-4336-abae-007d92ac6a08', 'Senior 1', '2023-02-10 20:42:12.039771', '2023-08-07 15:38:11.091311', false, 15);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('a0d3a765-92af-4af1-94c5-d496d84a449e', 'Senior 2', '2023-08-07 16:09:22.575630', '2023-08-07 16:09:22.575645', false, 15);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('6e65f32c-adde-4dc5-89e9-fd65dd2d5a0b', 'Semi Senior 2', '2023-08-07 16:09:36.154677', '2023-08-07 16:09:36.154688', false, 10);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('5cae589d-0e56-4aeb-b523-e794142c59d6', 'Semi Senior 1', '2023-08-07 16:09:45.676216', '2023-08-07 16:09:45.676236', false, 10);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('6b68500a-9e9c-4e19-9025-5666564f4574', 'Architect', '2023-08-07 16:09:54.867249', '2023-08-07 16:09:54.867260', false, 15);
INSERT INTO public.seniority (id, name, created_at, modified_at, deleted, vacation_days) VALUES ('a8b328a1-d6f1-49f0-b712-a3fe7fcc29bf', 'Trainee', '2023-08-07 16:10:04.937312', '2023-08-07 16:10:04.937323', false, 10);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('917aff98-a31b-4a5f-a0b8-dd938e54e20a', 'SQL', '2023-02-02 13:36:27.942958', '2023-02-02 13:36:27.942972', false);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('f7dc87e7-dfed-4445-a29a-600d62380fd6', 'React', '2023-02-02 13:36:32.878582', '2023-02-02 13:36:32.878596', false);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('33444f84-9d1a-498b-9ee6-2657c79ad7c1', 'Cypress', '2023-02-02 13:36:37.876545', '2023-02-02 13:36:37.876558', false);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('5c636e7d-43f7-4192-99af-430a8f9aec9e', 'NodeJS', '2023-02-02 13:36:51.597360', '2023-02-02 13:36:51.597375', false);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('507d096b-f82a-4c8e-9656-b595c0784561', 'Java edit test', '2023-02-02 13:36:22.692488', '2023-02-02 14:30:58.208303', true);
INSERT INTO public.technology (id, name, created_at, modified_at, deleted) VALUES ('41960cc8-350e-49a1-bb01-cbacb7fd752d', 'Java', '2023-02-10 20:42:02.185191', '2023-02-10 20:42:02.185206', false);
INSERT INTO public.tenant (id, name, display_name, created_at, modified_at, deleted) VALUES ('251141ab-298f-437b-a6ab-4984cafaefe0', 'entropy', 'Entropy Team', '2022-11-08 10:45:24.904104', '2022-11-08 10:45:24.904104', false);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('d7c426e4-23fc-45dc-b61a-f31cac958a60', '2023', 12, null, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-06-30 15:43:22.573875', '2023-06-30 15:43:30.346138', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('b7b04be4-2fdd-451a-a9ab-00850aaf6007', '2024', 4, null, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-06-30 15:43:51.582914', '2023-06-30 15:43:59.158640', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e783a9a0-3c6c-414b-9620-110620894e97', '2023', 15, null, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-07-03 19:21:37.537292', '2023-07-03 19:21:37.537305', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('779e5c40-7907-4604-aa81-94e4ca01c2ad', '2020', 10, null, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:13:34.232397', '2023-07-04 13:13:34.232415', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('c4fbe767-d0fc-418c-82b6-b05f9b41914e', '2021', 10, null, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:13:44.670817', '2023-07-04 13:13:44.670830', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('eeffff62-8055-4ac6-be9f-006db2d8bf59', '2022', 10, null, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:14:01.893865', '2023-07-04 13:14:01.893877', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('8e3eea2f-51c4-46c0-971b-7377daf8c48f', '2023', 15, null, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:14:12.303785', '2023-07-04 13:14:12.303799', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('76d5be2d-da85-4ff5-a646-4b8ff55e81bd', '2022', 10, null, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-07-13 13:44:36.869161', '2023-07-13 13:44:36.869183', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('09a3bbd6-ed37-405d-a899-743e8271d021', '2020', 0, 10, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:14:12.303785', '2023-07-04 13:14:12.303799', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('403ccdd9-2fa3-48a6-918a-afa09429b454', '2021', 0, 10, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:14:12.303785', '2023-07-04 13:14:12.303799', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('6934b949-d9fd-4ae6-9715-dd0283171f79', '2022', 0, 5, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-04 13:14:12.303785', '2023-07-04 13:14:12.303799', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('6df43429-5317-422f-808e-aa8933a8071f', '2023', 0, 6, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-25 14:28:43.546257', '2023-07-25 14:29:06.595234', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('cace42c3-f3f9-4470-8007-2510c5502439', '2022', 0, 2, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-20 16:09:11.519533', '2023-07-25 16:17:33.270278', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('bd238261-a15b-4ed1-bca1-3aebf3c649a7', '2023', 0, 1, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-25 16:17:33.310499', '2023-07-25 16:18:03.736589', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e5aafe40-f15f-456c-91cf-1a12b8eeccd1', '2022', 0, 2, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-25 16:17:33.308269', '2023-07-25 16:18:03.743800', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('32af673a-55ce-400e-bb6c-ed2e18d682d3', '2022', 12, null, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-06-30 15:43:42.530054', '2023-07-26 15:54:38.163139', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('541f0ee4-60b1-48f4-98e5-32cfd119ad3c', '2022', 0, 3, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-26 15:56:59.417937', '2023-07-26 16:18:34.249593', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('33c3c746-d0f6-4dbd-935e-e12329a7ece8', '2022', 0, 6, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-26 16:18:34.252623', '2023-07-26 16:20:46.973208', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('d97bfebe-dda8-474e-bf6a-b3edab10c4d0', '2022', 0, 1, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-07-26 13:57:46.113012', '2023-07-26 16:20:54.327717', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('bfb79f1f-b87a-4088-9668-fa25cc6a0c2b', '2023', 10, null, 'fb7c10dd-1db0-4d5d-af2b-799d874e7729', '2023-08-09 13:55:23.867045', '2023-08-09 13:55:23.867060', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('90a5d1e7-1e1d-45b4-8024-e46a724ee5d2', '2022', 12, null, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-07-03 19:21:27.060343', '2023-08-23 20:23:42.228841', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('f35c2671-e22e-41ad-8874-d741c8e299c0', '2022', 1, null, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-08-23 20:23:06.373569', '2023-08-23 20:23:51.495294', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('a46ccf9a-8b46-4393-b6bc-c7cc78622093', '2022', 0, 5, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-19 12:24:13.872200', '2023-08-24 12:34:41.433781', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('fa49df37-641f-48ba-897e-d83f03007718', '2021', 0, 3, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-19 12:22:17.067256', '2023-08-24 12:34:41.435455', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('9efec56b-a136-4dfc-928a-9a73aea71ab8', '2023', 15, 0, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '2023-11-17 15:29:58.050051', '2023-11-17 15:29:58.050062', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('8049f1a6-f82b-4f45-946c-ad2c3c7f1baa', '2021', 0, 2, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-19 12:24:13.866793', '2023-08-24 12:34:58.625430', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('589bf154-e5a5-4b27-a106-6a57d3f73200', '2020', 0, 10, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-07-19 12:22:17.047514', '2023-08-24 12:34:58.629450', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('c943bb71-8fa2-4239-a671-b63d7d6e07c3', '2024', 15, null, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-08-24 12:48:26.861022', '2023-08-24 12:48:26.861035', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('41878ead-7ebf-45ff-a2f2-e8be4920b8ee', '2024', 0, 3, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-08-24 12:48:55.395880', '2023-08-24 12:51:12.615040', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('eea0fb1d-617f-4a7c-b1b7-ac4d5b973986', '2024', 0, 3, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-08-24 12:52:43.039386', '2023-08-24 12:53:07.279018', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e05c32a2-db22-4502-98eb-3cb4c5f6f3fd', '2019', 1, null, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-09-01 22:01:28.656574', '2023-09-01 22:01:28.656587', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e1d19a0a-0a6d-4ba7-9870-d520daed52f0', '2023', 10, null, 'd102b786-7f8a-42b4-ab49-30b8390ee173', '2023-09-15 12:36:45.066849', '2023-09-15 12:36:45.066862', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('49b1507c-4619-4c77-a5eb-8048355b7b52', '2023', 0, 3, 'd102b786-7f8a-42b4-ab49-30b8390ee173', '2023-09-15 12:38:49.874415', '2023-09-15 12:38:49.874429', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('9fb03aa2-1b39-4d47-97c0-55fded931d02', '2024', 3, null, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-09-15 13:26:10.462893', '2023-09-15 13:26:10.462905', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('b2531688-36b3-44ad-a70a-fcabec73cdc8', '2022', 0, 5, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-07-26 16:20:46.979452', '2023-09-15 13:27:25.918755', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('46ed9387-b38a-4587-8b69-6a7a203cae3c', '2022', 0, 2, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-11-02 13:12:01.366641', '2023-11-02 13:12:01.366662', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('c221a6b9-dbd4-4441-ab71-2dadce4e3e3a', '2020', 0, 3, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-10-19 14:52:47.748619', '2023-11-07 19:08:02.812628', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('06b27397-9787-41bc-956f-5b5db64fd96a', '2020', 0, 6, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-11-07 19:17:52.178340', '2023-11-07 19:17:52.178352', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('6f96c4d2-d053-4673-8a06-b781f429af4d', '2020', 0, 1, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2023-11-07 19:18:44.376714', '2023-11-07 19:18:44.376726', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('a72fed0b-6afd-4639-952c-58561184c586', '2021', 0, null, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-11-07 19:49:44.909185', '2023-11-07 19:49:44.909197', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('51b9389a-7f5d-423a-99df-d63ece622ff7', '2024', 10, 0, 'f5a61455-0e7d-434c-b510-233b087726e2', '2024-01-04 12:32:31.759536', '2024-01-04 12:32:31.759553', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('0455a2eb-fa8c-460d-95db-00a629bb2ca3', '2022', 0, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '2023-11-17 14:47:17.921230', '2023-11-17 14:47:17.921245', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e2695731-2f1f-4f81-9c4f-2fa1baf516f8', '2023', 15, 0, 'f03bab93-5582-49f7-a04a-e1332f86361f', '2023-11-17 14:47:23.610343', '2023-11-17 14:47:23.610354', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('492dddf7-1734-498f-bef9-46b85bdb6c3e', '2023', 0, 6, 'f03bab93-5582-49f7-a04a-e1332f86361f', '2023-11-17 14:47:39.813887', '2023-11-17 14:47:39.813898', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('f6bb34d4-54a1-4d1b-acb0-39be6fa9a0a2', '2022', 0, 0, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '2023-11-17 15:29:52.510621', '2023-11-17 15:29:52.510631', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('5ce5401d-fc4d-4bdf-846f-0ca3c5657489', '2023', 0, 5, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '2023-11-17 15:30:09.759783', '2023-11-17 15:30:56.454976', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('da540fe1-66de-4c9d-b06a-c5cf6d9059bb', '2023', 0, 4, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '2023-11-17 15:31:22.498415', '2023-11-17 15:31:54.543452', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('44e1e13d-74ca-4578-84cf-ef8e0fe68772', '2023', 0, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-11-17 19:31:41.929299', '2023-11-17 19:31:41.929310', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('91bdd1cc-9373-461d-8de1-a9a9f81ed38b', '2022', 0, 0, '52673af1-f94e-474d-ad03-58398421b92a', '2023-11-21 22:00:04.397441', '2023-11-21 22:00:04.397458', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('b2915c8c-d629-4d2d-9430-e77d4405b9ec', '2023', 10, 0, '52673af1-f94e-474d-ad03-58398421b92a', '2023-11-21 22:00:12.547784', '2023-11-21 22:00:12.547801', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('216b4e52-eaf5-4de1-99ac-25dcb0325adb', '2023', 0, 3, '52673af1-f94e-474d-ad03-58398421b92a', '2023-11-21 22:00:28.568403', '2023-11-21 22:17:11.187429', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('51b97c1d-5663-4190-b18e-a87ec5b0f37b', '2022', 5, 5, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2023-09-15 13:26:55.711144', '2023-11-21 23:40:41.110323', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('e1db78b5-8148-4250-931c-45f6e2ef22bb', '2022', 0, 0, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '2023-11-24 13:35:54.784448', '2023-11-24 13:35:54.784457', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('8bef8dd6-e41f-4f42-80bd-747c8f93ef20', '2023', 15, 0, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '2023-11-24 13:36:02.312088', '2023-11-24 13:36:02.312099', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('2c149c00-a7db-45cb-9b5d-f757474a3bac', '2023', 0, 3, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '2023-11-24 13:36:33.001215', '2023-11-24 13:36:45.649541', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('a656e69c-ccb0-4b14-9af8-bb733353ed91', '2024', 0, 3, 'f5a61455-0e7d-434c-b510-233b087726e2', '2024-01-05 14:52:32.719655', '2024-01-05 14:53:02.874911', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('eb4f6779-f9a8-4a81-87d3-60a664a107f7', '2022', 0, 4, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-08-23 20:24:34.484775', '2023-12-06 13:35:53.392641', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('1665894d-16dc-4c81-966f-bdf90d2441fa', '2019', 0, 1, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-10-27 13:24:05.810703', '2023-12-06 13:35:53.400077', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('41261e0b-de4d-4a1d-af26-de2d10eecf24', '2019', 0, 1, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-12-06 13:35:53.431276', '2023-12-06 13:35:53.431300', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('889cea3f-8191-441f-8c73-37db292e005e', '2022', 0, 5, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2023-12-06 13:35:53.432795', '2023-12-06 13:35:53.432810', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('19a2d9cb-1201-4e39-9ed3-61dfcf945817', '2023', 10, 0, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '2023-12-06 13:37:21.348084', '2023-12-06 13:37:21.348098', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('1081faa8-f7f7-4299-9bd3-cea353b7dfc1', '2023', 0, 3, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '2023-12-06 13:37:34.018164', '2023-12-06 13:37:34.018178', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('554bbb3b-bb9e-43cd-bed2-c2630dfb5561', '2024', 0, 4, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-08-24 12:53:40.032417', '2023-12-13 13:12:21.681049', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('36512c26-d5dd-4170-b7f1-a3bfe317d228', '2024', 0, 5, '001a8e97-069f-471c-bd80-dd168f5cdbfd', '2023-12-13 13:12:21.720985', '2023-12-13 13:12:21.720997', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('277aac62-4523-4731-91bf-501552f03a1f', '2024', 15, 0, '09f38a52-e6fe-4f24-b39a-d1ec507eca61', '2023-12-21 19:54:12.711469', '2023-12-21 19:54:12.711480', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('816e9e8b-2225-463b-b8e1-e49ecfb05e6d', '2023', 12, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2023-10-20 13:23:39.263407', '2024-01-05 13:20:47.529301', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('fe952cef-ac41-4787-a47b-41a8241284a6', '2024', 15, 0, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2024-01-05 13:21:20.799537', '2024-01-05 13:21:20.799550', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('16909f6a-f60a-47ee-a6bd-39f9bcecab46', '2024', 10, 0, 'fd5b20f6-33e9-4180-8da5-c8db9a7c299b', '2024-01-05 13:36:55.849130', '2024-01-05 13:36:55.849145', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('c94a8a0f-7bfc-41cb-b919-d46dcb9158b7', '2024', 0, 3, 'fd5b20f6-33e9-4180-8da5-c8db9a7c299b', '2024-01-05 13:37:39.167642', '2024-01-05 13:37:39.167669', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('d7fe84c3-5ccb-4d20-b7f2-ea0bdcc4129d', '2024', 0, 3, 'f5a61455-0e7d-434c-b510-233b087726e2', '2024-01-05 14:49:30.836325', '2024-01-05 14:50:11.414981', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('f3a098b8-1332-4cd7-abb3-34d997083e6a', '2024', 0, 5, 'f5a61455-0e7d-434c-b510-233b087726e2', '2024-01-05 14:53:02.879427', '2024-01-05 14:53:38.921514', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('6f5e8553-57c4-476c-8db6-5c0863a0c7ac', '2022', 0, 3, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2024-01-05 15:00:53.580092', '2024-01-05 15:00:53.580102', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('eaacfa6c-489a-446d-af0e-82bcc2c56863', '2023', 2, 0, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2024-02-29 21:29:31.852329', '2024-02-29 21:29:31.852355', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('a286a68f-bb63-46f2-aa91-2826f0ae5689', '2023', 0, 12, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2024-03-11 15:51:42.591535', '2024-03-11 15:51:42.591550', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('f2e0dca3-9263-4f3d-ab4f-f77717c986b7', '2023', 0, 7, '5fa672f5-420e-4737-b2e1-f50fd00120a2', '2024-03-11 15:51:42.621955', '2024-03-11 15:51:42.621965', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('a618929e-21a0-4923-a89e-29df77d7211a', '2023', 0, 7, '20e6eee0-abd5-401d-9ad1-ee2c4f84e408', '2024-03-11 15:51:42.628101', '2024-03-11 15:51:42.628108', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('319694f4-4c80-4645-8862-caf28aae27b5', '2023', 0, 10, '2c83734b-e42e-4ab0-acce-ba372b0e2744', '2024-03-11 15:51:42.632088', '2024-03-11 15:51:42.632096', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('985c43a7-68b7-41cc-98f1-3c460fe95784', '2023', 0, 5, '52673af1-f94e-474d-ad03-58398421b92a', '2024-03-11 15:51:42.636796', '2024-03-11 15:51:42.636804', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('b640027a-95a8-4514-bb52-eef0847ffd10', '2023', 0, 10, 'bf402384-44f1-49a7-b9e7-71b841b753c1', '2024-03-11 15:51:42.647233', '2024-03-11 15:51:42.647241', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('f4015ed0-a0d2-4eef-ad18-0ad8cb59698d', '2023', 0, 4, 'f03bab93-5582-49f7-a04a-e1332f86361f', '2024-03-11 15:51:42.652820', '2024-03-11 15:51:42.652828', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('3b54ac35-9070-463d-a7db-016780fd4767', '2023', 0, 2, '4a5b381b-68b1-4bb0-bc82-6c5009a8926b', '2024-03-11 15:51:42.663826', '2024-03-11 15:51:42.663834', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('adeda215-d812-4369-979e-a5fba702798c', '2023', 0, 10, '24005b6b-718e-4926-b9f5-ac35b5952a7b', '2024-03-11 15:51:42.666803', '2024-03-11 15:51:42.666811', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('6944b105-e7a7-4ffd-9f52-931084614136', '2023', 0, 2, 'd102b786-7f8a-42b4-ab49-30b8390ee173', '2024-03-11 15:51:42.669767', '2024-03-11 15:51:42.669775', false, 'EXPIRED');
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('25aa556e-f914-4c9d-b549-92fa50f3f5f6', '2022', 0, 2, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2024-03-21 20:38:51.555723', '2024-03-21 20:38:51.555737', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('c2edc045-9c1b-4f3f-86fb-77aacc2da378', '2023', 0, 2, '03530cf8-d267-48a3-9c1b-f2dd47894e96', '2024-03-21 20:38:51.582648', '2024-03-21 20:38:51.582657', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('4bdc9f14-7c7c-43e4-ab77-7722fc57b25e', '2023', 10, 0, '49484990-2dcf-4131-bacc-23a2e8e04916', '2024-04-24 20:46:46.109409', '2024-04-24 20:46:46.109629', false, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('bc2ab8bc-a0d0-48a1-88a7-d22e11fea4e6', '2023', 0, 3, '49484990-2dcf-4131-bacc-23a2e8e04916', '2024-04-24 20:47:52.962948', '2024-04-24 20:48:02.839129', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('ee748ec1-586b-4914-b40c-ecf87f70b519', '2023', 0, 2, '49484990-2dcf-4131-bacc-23a2e8e04916', '2024-04-24 20:48:02.845227', '2024-04-24 21:02:21.424034', true, null);
INSERT INTO public.vacation (id, year, credit, debit, employee_id, created_at, modified_at, deleted, details) VALUES ('5ba77e60-8a44-4258-a639-ff948554d731', '2023', 0, 2, '49484990-2dcf-4131-bacc-23a2e8e04916', '2024-04-24 21:05:37.963495', '2024-04-24 21:05:37.963507', false, null);
SET session_replication_role = 'origin';
COMMIT;