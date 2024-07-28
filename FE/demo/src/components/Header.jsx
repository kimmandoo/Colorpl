import React, { useState, useEffect } from 'react';
import { Box, Flex, Button, Icon, Avatar, useToast, Input, VStack, Collapse, Text } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { MdOutlineAdminPanelSettings } from "react-icons/md";
import { IoMdExit } from 'react-icons/io';
import api from '../api';

const Header = () => {
  const navigate = useNavigate();
  const [profile, setProfile] = useState({});
  const [fileInput, setFileInput] = useState(null);
  const [isOpen, setIsOpen] = useState(false);
  const toast = useToast();

  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem('token');
      try {
        const response = await api.get('/auth/administrators/me', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setProfile(response.data);
      } catch (error) {
        console.error('Failed to fetch profile', error);
      }
    };

    fetchProfile();
  }, []);

  const handleLogout = async () => {
    const token = localStorage.getItem('token');
    try {
      await api.post('/auth/logout', null, {
        headers: { Authorization: `Bearer ${token}` }
      });
      localStorage.removeItem('token');
      toast({
        description: "Please come again ~",
        status: 'success',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
      navigate('/');
    } catch (error) {
      toast({
        description: 'Logout failed',
        status: 'error',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
    }
  };

  const handleImageUpload = async () => {
    if (!fileInput) {
      toast({
        description: '파일을 선택해주세요.',
        status: 'error',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
      return;
    }

    const token = localStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', fileInput);

    try {
      const response = await api.post('/auth/profile/upload_image', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      toast({
        description: "프로필 이미지가 성공적으로 업로드되었습니다.",
        status: 'success',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
      setProfile({ ...profile, profile: { ...profile.profile, image_url: response.data.image_url } });
    } catch (error) {
      toast({
        description: 'Image upload failed',
        status: 'error',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
    }
  };

  const handleAvatarClick = () => {
    setIsOpen(!isOpen);
  };

  const handleDeleteRequest = async () => {
    const token = localStorage.getItem('token');
    try {
      await api.post('/auth/admin/delete_request', null, {
        headers: { Authorization: `Bearer ${token}` }
      });
      toast({
        description: "탈퇴 신청이 성공적으로 요청되었습니다.",
        status: 'success',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
    } catch (error) {
      toast({
        description: error.response?.data.detail || 'Delete request failed',
        status: 'error',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
    }
  };

  const getRoleText = (role) => {
    switch (role) {
      case 1:
        return '슈퍼 어드민';
      case 2:
        return '치프 어드민';
      case 3:
        return '서브 어드민';
      default:
        return '알 수 없음';
    }
  };

  return (
    <Box bg="gray.800" color="white" px={4} py={2} position="fixed" top="0" width="100%" zIndex="1000">
      <Flex justify="flex-end" align="center">
        <Button colorScheme="red" onClick={handleLogout} mr={4}>
          <Icon as={IoMdExit} boxSize={6} />
        </Button>
        <Box position="relative">
          <Avatar
            size="md"
            src={profile.profile?.image_url}
            icon={!profile.profile?.image_url && <MdOutlineAdminPanelSettings size="1.5em" />}
            cursor="pointer"
            onClick={handleAvatarClick}
          />
          <Collapse in={isOpen} animateOpacity={false} style={{ position: 'absolute', top: 'calc(100% + 10px)', right: 0, width: '200px', background: 'white', color: 'black', boxShadow: 'md', borderRadius: 'md', zIndex: 1000 }}>
            <VStack spacing={2} p={4} align="stretch">
              <Text><strong>이름:</strong> {profile.username}</Text>
              <Text><strong>등급:</strong> {getRoleText(profile.role)}</Text>
              <Input type="file" display="none" id="fileInput" onChange={(e) => setFileInput(e.target.files[0])} />
              <Button colorScheme="blue" onClick={() => document.getElementById('fileInput').click()}>
                {profile.profile?.image_url ? '이미지 수정' : '이미지 저장'}
              </Button>
              {profile.role !== 1 && (
                <Button colorScheme="red" onClick={handleDeleteRequest}>
                  탈퇴 신청
                </Button>
              )}
            </VStack>
          </Collapse>
        </Box>
      </Flex>
    </Box>
  );
};

export default Header;
