import React, { useState, useEffect } from 'react';
import {
  Box, Table, Thead, Tbody, Tr, Th, Td, Input, IconButton, Modal,
  ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter, Button, useDisclosure, Textarea, NumberInput, NumberInputField
} from '@chakra-ui/react';
import { SearchIcon, ViewIcon } from '@chakra-ui/icons';
import axios from 'axios';
import { useAppContext } from './AppContext';

const UserManagementPage = () => {
  const { admin } = useAppContext();
  const [members, setMembers] = useState([]);
  const [search, setSearch] = useState('');
  const [selectedMember, setSelectedMember] = useState(null);
  const [managementReason, setManagementReason] = useState('');
  const [banDays, setBanDays] = useState('0');
  const [banHours, setBanHours] = useState('0');
  const { isOpen, onOpen, onClose } = useDisclosure();

  useEffect(() => {
    if (admin) {
      fetchMembers();
    }
  }, [admin]);

  const fetchMembers = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8000/cs/members', {
        params: {
          skip: 0,
          limit: 100,
          order_by: 'created_date', // 최근 생성된 유저를 가져오기 위한 파라미터
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setMembers(response.data.data);
    } catch (error) {
      console.error('Error fetching members:', error);
    }
  };

  const handleSearch = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8000/cs/members', {
        params: {
          nickname: search,
          skip: 0,
          limit: 100,
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setMembers(response.data.data);
    } catch (error) {
      console.error('Error searching members:', error);
    }
  };

  const handleViewDetails = (member) => {
    setSelectedMember(member);
    onOpen();
  };

  const handleStatusChange = async (memberId, isDeleted, managementReason, banDays, banHours) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8000/cs/members/${memberId}/status`, {
        is_deleted: isDeleted,
        management_reason: managementReason,
        ban_duration: parseInt(banDays, 10) * 24 + parseInt(banHours, 10),  // Total duration in hours
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      fetchMembers();
      onClose();
    } catch (error) {
      console.error('Error updating member status:', error);
    }
  };

  return (
    <Box p={4}>
      <h1>사용자 관리 페이지</h1>
      <Box mb={4} display="flex">
        <Input
          placeholder="닉네임 검색"
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
            <Th>닉네임</Th>
            <Th>이메일</Th>
            <Th>관리</Th>
          </Tr>
        </Thead>
        <Tbody>
          {members.map((member) => (
            <Tr key={member.member_id}>
              <Td>{member.member_id}</Td>
              <Td>{member.nickname}</Td>
              <Td>{member.email}</Td>
              <Td>
                <IconButton
                  icon={<ViewIcon />}
                  onClick={() => handleViewDetails(member)}
                />
                {member.is_deleted && (
                  <Button colorScheme="green" onClick={() => handleStatusChange(member.member_id, false, 'Unban', 0, 0)}>
                    차단 해제
                  </Button>
                )}
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      {selectedMember && (
        <Modal isOpen={isOpen} onClose={onClose}>
          <ModalOverlay />
          <ModalContent>
            <ModalHeader>사용자 상세 정보</ModalHeader>
            <ModalCloseButton />
            <ModalBody>
              <p>닉네임: {selectedMember.nickname}</p>
              <p>이메일: {selectedMember.email}</p>
              <Textarea
                placeholder="관리 사유를 입력하세요"
                value={managementReason}
                onChange={(e) => setManagementReason(e.target.value)}
                mt={4}
              />
              <NumberInput
                value={banDays}
                onChange={(valueString) => setBanDays(valueString)}
                mt={4}
              >
                <NumberInputField placeholder="차단 기간 (일)" />
              </NumberInput>
              <NumberInput
                value={banHours}
                onChange={(valueString) => setBanHours(valueString)}
                mt={4}
              >
                <NumberInputField placeholder="차단 기간 (시간)" />
              </NumberInput>
            </ModalBody>
            <ModalFooter>
              <Button colorScheme="red" mr={3} onClick={() => handleStatusChange(selectedMember.member_id, true, managementReason, banDays, banHours)}>
                일시 차단
              </Button>
              <Button variant="ghost" onClick={onClose}>닫기</Button>
            </ModalFooter>
          </ModalContent>
        </Modal>
      )}
    </Box>
  );
};

export default UserManagementPage;
