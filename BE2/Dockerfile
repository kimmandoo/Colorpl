# Use an official Python runtime as a parent image
FROM python:3.10.14-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file into the container at /app
COPY requirements.txt requirements.txt

# Install any dependencies specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the working directory contents into the container at /app
COPY . .

# Run tests
RUN pytest

# Make port 80 available to the world outside this container
EXPOSE 80

# Run uvicorn server on container start
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "80"]
