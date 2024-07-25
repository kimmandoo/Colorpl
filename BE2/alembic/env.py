import os
import sys
from logging.config import fileConfig
from sqlalchemy import create_engine, pool
from alembic import context
from dotenv import load_dotenv
from urllib.parse import quote_plus

# Load environment variables
load_dotenv()

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

# this is the Alembic Config object, which provides access to the values within the .ini file in use.
config = context.config

# Interpret the config file for Python logging.
if config.config_file_name is not None:
    fileConfig(config.config_file_name)

# Add your model's MetaData object here for 'autogenerate' support
from auth.models import Base
target_metadata = Base.metadata

# Get database credentials from environment variables and encode password
db_user = os.getenv('DATABASE_USER')
db_password = quote_plus(os.getenv('DATABASE_PASSWORD'))  # URL encode the password
db_host = os.getenv('DATABASE_HOST')
db_name = os.getenv('DATABASE_NAME')

# Construct the SQLAlchemy database URL
sqlalchemy_url = f"mysql+pymysql://{db_user}:{db_password}@{db_host}/{db_name}"

# Define the run_migrations_offline function
def run_migrations_offline() -> None:
    """Run migrations in 'offline' mode."""
    context.configure(
        url=sqlalchemy_url,
        target_metadata=target_metadata,
        literal_binds=True,
        dialect_opts={"paramstyle": "named"},
    )

    with context.begin_transaction():
        context.run_migrations()

# Define the run_migrations_online function
def run_migrations_online() -> None:
    """Run migrations in 'online' mode."""
    connectable = create_engine(
        sqlalchemy_url,
        poolclass=pool.NullPool,
    )

    with connectable.connect() as connection:
        context.configure(
            connection=connection, target_metadata=target_metadata
        )

        with context.begin_transaction():
            context.run_migrations()

# Determine the mode (offline/online) and run the appropriate function
if context.is_offline_mode():
    run_migrations_offline()
else:
    run_migrations_online()
