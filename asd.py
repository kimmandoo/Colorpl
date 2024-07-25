def convert_to_crlf(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    content = content.replace('\n', '\r\n')
    with open(file_path, 'w', encoding='utf-8', newline='') as file:
        file.write(content)

# 파일 경로를 적절히 수정하세요.
alembic_ini_path = 'BE2/alembic.ini'
env_py_path = 'BE2/alembic/env.py'

convert_to_crlf(alembic_ini_path)
convert_to_crlf(env_py_path)

print(f'Converted {alembic_ini_path} and {env_py_path} to CRLF format.')
