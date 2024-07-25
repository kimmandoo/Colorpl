"""2nd

Revision ID: 475ec85850d2
Revises: 78795b63d34b
Create Date: 2024-07-25 13:31:07.495972

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = '475ec85850d2'
down_revision: Union[str, None] = '78795b63d34b'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    pass


def downgrade() -> None:
    pass
