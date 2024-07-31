# Use an official Python runtime as a parent image
FROM python:3.10.14-slim

# Set the working directory in the container
WORKDIR /BE2

# Copy the requirements file into the container at /app
COPY requirements.txt /BE2/requirements.txt

# Install any dependencies specified in requirements.txt
RUN pip install --no-cache-dir -r /BE2/requirements.txt

# Copy the rest of the working directory contents into the container at /app
COPY ./BE2 /BE2

# Run tests
RUN pytest

# Make port 80 available to the world outside this container
EXPOSE 80

# Run uvicorn server on container start
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "80"]
