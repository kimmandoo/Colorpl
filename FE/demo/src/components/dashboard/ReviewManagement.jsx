import React, { useState, useEffect } from 'react';
import {
  Box, Table, Thead, Tbody, Tr, Th, Td, Button, Textarea, Input, IconButton, useToast, useDisclosure, Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter
} from '@chakra-ui/react';
import { SearchIcon } from '@chakra-ui/icons';
import axios from 'axios';
import { useAppContext } from './AppContext';

const ReviewManagementPage = () => {
  const { admin } = useAppContext();
  const [reviews, setReviews] = useState([]);
  const [search, setSearch] = useState('');
  const [selectedReview, setSelectedReview] = useState(null);
  const [managementReason, setManagementReason] = useState('');
  const toast = useToast();
  const { isOpen, onOpen, onClose } = useDisclosure();

  useEffect(() => {
    if (admin) {
      fetchReviews();
    }
  }, [admin]);

  const fetchReviews = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8000/cs/reviews', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setReviews(response.data);
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  const handleSearch = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8000/cs/reviews', {
        params: { content: search },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setReviews(response.data);
    } catch (error) {
      console.error('Error searching reviews:', error);
    }
  };

  const handleViewReview = async (reviewId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8000/cs/reviews/${reviewId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setSelectedReview(response.data);
      onOpen();
    } catch (error) {
      console.error('Error fetching review:', error);
    }
  };

  const handleSpoilerReview = async (reviewId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8000/cs/reviews/${reviewId}`, null, {
        params: {
          is_spoiler: true,
          management_reason: managementReason
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      toast({
        title: "Review marked as spoiler.",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      fetchReviews(); // 리뷰 목록 갱신
      onClose(); // 모달 닫기
      setManagementReason(''); // 관리 사유 초기화
    } catch (error) {
      console.error('Error updating review:', error);
      toast({
        title: "Error marking review as spoiler.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };
  
  return (
    <Box p={4}>
      <h1>리뷰 관리 페이지</h1>
      <Box mb={4} display="flex">
        <Input
          placeholder="리뷰 내용 검색"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <IconButton
          icon={<SearchIcon />}
          onClick={handleSearch}
          ml={2}
        />
      </Box>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>ID</Th>
            <Th>Content</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {reviews.map((review) => (
            <Tr key={review.review_id}>
              <Td>{review.review_id}</Td>
              <Td>{review.content}</Td>
              <Td>
                <Button onClick={() => handleViewReview(review.review_id)}>View</Button>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      {selectedReview && (
        <Modal isOpen={isOpen} onClose={onClose}>
          <ModalOverlay />
          <ModalContent>
            <ModalHeader>리뷰 상세 정보</ModalHeader>
            <ModalCloseButton />
            <ModalBody>
              <p>ID: {selectedReview.review_id}</p>
              <p>Content: {selectedReview.is_spoiler ? "스포일러 리뷰입니다" : selectedReview.content}</p>
              <Textarea
                placeholder="관리 사유를 입력하세요"
                value={managementReason}
                onChange={(e) => setManagementReason(e.target.value)}
                mt={4}
              />
            </ModalBody>
            <ModalFooter>
              <Button colorScheme="red" mr={3} onClick={() => handleSpoilerReview(selectedReview.review_id)}>Mark as Spoiler</Button>
              <Button variant="ghost" onClick={onClose}>닫기</Button>
            </ModalFooter>
          </ModalContent>
        </Modal>
      )}
    </Box>
  );
};

export default ReviewManagementPage;
